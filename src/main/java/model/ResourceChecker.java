package model;

import data.DataReader;
import lib.Class;
import lib.Graph;
import lib.Slot;
import lib.SlotGroup;
import lib.Teacher;

import java.util.Vector;

public class ResourceChecker {
    private static Vector<Integer> getClassBySlot(Vector<lib.Class> classes, int slotId) {
        Vector<Integer> res = new Vector<>();
        for (Class c : classes) {
            if (c.getSlotId() == slotId) {
                res.add(c.getId());
            }
        }

        return res;
    }
    public static boolean checkEnoughResource(Model model) {
        Vector<Graph.Edge> edges = new Vector<>();

        Vector<Slot> slots = SlotGroup.getSlotList(model.getSlots());

        int source = model.getTeachers().size() * (slots.size() + 1) + model.getClasses().size();
        int sink = source + 1;
        int superSource1 = sink + 1;
        int superSource = superSource1 + 1;
        int superSink = superSource + 1;

        Dinic dinic = new Dinic(superSink + 1, superSource, superSink);
        for (int i = 0; i < model.getTeachers().size(); i++) {
            edges.add(new Graph.Edge(source, i, model.getTeachers().get(i).getQuota(), Dinic.INF));
        }

        for (int i = 0; i < model.getClasses().size(); i++) {
            edges.add(new Graph.Edge(model.getTeachers().size() * (1 + slots.size()) + i, sink, 0, 1));
        }

        for (int i = 0; i < model.getTeachers().size(); i++) {
            for (int j = 0; j < slots.size(); j++) {
                if (model.getRegisteredSlots()[i][j] > 0) dinic.add(i, model.getTeachers().size() + i * slots.size() + j, 1);
            }
        }

        for (int j = 0; j < slots.size(); j++) {
            for (int i = 0; i < model.getTeachers().size(); i++) {
                Vector<Integer> classes = getClassBySlot(model.getClasses(), j);
                for(int classId:classes) {
                    int subjectId = model.getClasses().get(classId).getSubjectId();
                    if (model.getRegisteredSubjects()[i][subjectId] > 0) {
                        edges.add(new Graph.Edge(model.getTeachers().size() + i * slots.size() + j,
                                model.getTeachers().size() * (1 + slots.size()) + classId, 0, 1));
                    }
                }
            }
        }


        for (Graph.Edge edge : edges) {
            dinic.add(edge.u, edge.v, edge.d, edge.c);
        }
        dinic.add(sink, superSource1, Dinic.INF);

        int totalDemand = 0;
        for (int i = 0; i < model.getTeachers().size(); i++) {
            totalDemand += model.getTeachers().get(i).getQuota();
        }
        dinic.add(superSource1, source, model.getClasses().size(), Dinic.INF);

        int fl = dinic.maxflow();
        System.out.println(fl + " " + model.getClasses().size() + " " + totalDemand);
        if (fl == model.getClasses().size() + totalDemand) {
            return true;
        } else return false;
    }

    public static void main(String[] args) {
        Model model = DataReader.getData();
        System.out.println(ResourceChecker.checkEnoughResource(model));
    }
}
