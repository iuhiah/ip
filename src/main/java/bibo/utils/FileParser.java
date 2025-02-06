package bibo.utils;

import bibo.exceptions.TaskFormatException;

/**
 * Parses data from file.
 */
public class FileParser {
    /**
     * Parses task data from file.
     *
     * @param taskData Task data from file.
     * @param isDone Whether task is done.
     */
    public static String[] parseTaskData(String taskData, boolean isDone) {
        String taskType = null;
        StringBuilder parsedDataString = new StringBuilder();

        try {
            String[] splitTaskData = taskData.split(" \\| ", 3);

            taskType = splitTaskData[0];
            switch (taskType) {
            case "T":
                taskType = "todo";
                break;
            case "D":
                taskType = "deadline";
                splitTaskData[2] = splitTaskData[2]
                        .replace(" (by: ", " /by ")
                        .replace(")", "");
                break;
            case "E":
                taskType = "event";
                splitTaskData[2] = splitTaskData[2]
                        .replace(" (from: ", " /from ")
                        .replace(" to ", " /to ")
                        .replace(")", "");
                break;
            default:
                throw new TaskFormatException(
                    TaskFormatException.ErrorType.UNKNOWN_TASK_TYPE.toString()
                );
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }

        return new String[] { taskType, parsedDataString.toString() };
    }
}
