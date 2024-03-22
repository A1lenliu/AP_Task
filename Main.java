import utm.*;

import java.awt.Color;

public class Main {
    public static void main(String[] args) {

        if (args.length != 3) {
            System.out.println("Usage: java Main <file_path> <input_string> <flag>");
            return;
        }

        String filePath = args[0];
        String inputString = args[1];
        String flag = args[2];

        UniversalTuringMachine utm = new UniversalTuringMachine();

        boolean isAnimationEnabled = flag.equals("--animation");

        Tape tape = new Tape(inputString);

        Head head = new Head(inputString);

        utm.loadInput(inputString);

        if (isAnimationEnabled) {
            HeadPanel headPanel = new HeadPanel(utm.getTuringMachine().getHead());
            TapePanel tapePanel = new TapePanel(utm.getTuringMachine().getTape());

            utm.getContentPane().add(headPanel);
            utm.getContentPane().add(tapePanel);

            utm.setSize(Config.WINDOW_WIDTH, Config.WINDOW_HEIGHT);
            utm.setResizable(false);
            utm.getContentPane().setBackground(new Color(203, 197, 197));

            utm.setVisible(true);
        } else {
            TuringMachine tm = utm.getTuringMachine();

            String initialState = tm.getInitialState();
            String acceptState = tm.getAcceptState();
            String rejectState = tm.getRejectState();

            String[][] rules = tm.getRules();

            String currentState = initialState;

            int headPosition = 0;

            while (!currentState.equals(acceptState) && !currentState.equals(rejectState)) {
                char currentSymbol = tape.get(headPosition);

                String newState = null;
                char newSymbol = 0;
                MoveClassical move = null;

                for (String[] rule : rules) {
                    if (rule[0].equals(currentState) && rule[1].charAt(0) == currentSymbol) {
                        newState = rule[2];
                        newSymbol = rule[3].charAt(0);
                        move = MoveClassical.valueOf(rule[4]);
                        break;
                    }
                }

                if (newState != null && move != null) {
                    currentState = newState;

                    tape.set(headPosition, newSymbol);

                    // 根据移动方向更新头部的位置
                    if (move == MoveClassical.RIGHT) {
                        headPosition++;
                    } else if (move == MoveClassical.LEFT) {
                        headPosition--;
                    }
                } else {
                    break;
                }
            }

            if (currentState.equals(acceptState)) {
                System.out.println("Accepted");
            } else if (currentState.equals(rejectState)) {
                System.out.println("Rejected");
            } else {
                System.out.println("Halted");
            }
        }
    }
}
