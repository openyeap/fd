package cn.zhumingwu.research.algorithm;

import lombok.extern.slf4j.Slf4j;

/*
 *  Breadth-First Search
 */
@Slf4j
public class PrintMatrix {

    // 根据条件创建图
    String[][] m =
            new String[][]{
                    {"F1", "F2", "F3", "F4", "F5", "F6", "F7"},
                    {"F1", "F2", "F3", "F4", "F5", "F6", "F7"},
                    {"F1", "F2", "F3", "F4", "F5", "F6", "F7"},
                    {"F1", "F2", "F3", "F4", "F5", "F6", "F7"},
                    {"F2", "F2", "F3", "F4", "F5", "F6", "F8"},
                    {"F3", "F2", "F3", "F4", "F5", "F6", "F9"},
                    {"F4", "F5", "F6", "F7", "F8", "F9", "F1"}
            };

    public void demo(String[] args) {

        print_matrix(m);
    }

    void print_matrix(String[][] m) {
        int sx = 0;
        int sy = 0;
        int ex = m[0].length - 1;
        int ey = m.length - 1;

        Rect rect = new Rect(sx, sy, ex, ey);
        while (rect != null) {
            System.out.printf("print from [%d,%d] to [%d,%d]\n", rect.sx, rect.sy, rect.ex, rect.ey);
            rect = printa(rect.sx, rect.sy, rect.ex, rect.ey);
            if (rect == null) {
                break;
            }
            System.out.printf("print from [%d,%d] to [%d,%d]\n", rect.sx, rect.sy, rect.ex, rect.ey);
            rect = printb(rect.sx, rect.sy, rect.ex, rect.ey);
        }
    }

    Rect printa(int sx, int sy, int ex, int ey) {
        Boolean hasPrint = false;
        for (int x = sx; x <= ex; x++) {
            System.out.printf("%s \t", m[sy][x]);
            hasPrint = true;
        }
        log.info("");
        if (hasPrint == false) {
            return null;
        }
        hasPrint = false;
        for (int y = sy + 1; y <= ey; y++) {
            System.out.printf("%s \t", m[y][ex]);
            hasPrint = true;
        }
        log.info("");
        if (hasPrint == false) {
            return null;
        }

        Rect result = new Rect(ex - 1, ey, sx, sy + 1);
        return result;
    }

    Rect printb(int sx, int sy, int ex, int ey) {
        Boolean hasPrint = false;
        for (int x = sx; x >= ex; x--) {
            System.out.printf("%s \t", m[sy][x]);
            hasPrint = true;
        }
        log.info("");
        if (hasPrint == false) {
            return null;
        }
        for (int y = sy - 1; y >= ey; y--) {
            System.out.printf("%s \t", m[y][ex]);
            hasPrint = true;
        }
        log.info("");
        if (hasPrint == false) {
            return null;
        }
        Rect result = new Rect(ex + 1, ey, sx, sy - 1);
        return result;
    }

    class Rect {
        int sx;
        int sy;
        int ex;
        int ey;

        public Rect(int sx, int sy, int ex, int ey) {
            this.ex = ex;
            this.ey = ey;
            this.sx = sx;
            this.sy = sy;
        }

        public int getSx() {
            return sx;
        }

        public int getSy() {
            return sy;
        }

        public int getEx() {
            return ex;
        }

        public int getEy() {
            return ey;
        }
    }
}
