package bibo.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import bibo.exceptions.TaskFormatException;

/**
 * Parses data from file.
 */
public class FileParser {
    /**
     * Parses task data from file.
     *
     * @param taskData Task data from file.
     * @return Parsed task data {taskType, taskData, isDone}.
     * @throws TaskFormatException If task data format is invalid.
     */
    public static String[] parseTaskData(String taskData) throws TaskFormatException {
        String taskType = null;
        String isDone = "false";

        // using regex to parse
        String regex = "^\\[(?<taskType>[TDE])\\]\\[(?<isDone>[X ])\\] (?<taskData>[\\S]+.*)$";
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(taskData);

        try {
            if (matcher.find()) {
                taskType = matcher.group("taskType");
                isDone = (matcher.group("isDone").equals("X")) ? "true" : "false";
                taskData = matcher.group("taskData");

                switch (taskType) {
                case "T":
                    taskType = "todo";
                    break;
                case "D":
                    taskType = "deadline";
                    break;
                case "E":
                    taskType = "event";
                    break;
                default:
                    throw new TaskFormatException(
                        TaskFormatException.ErrorType.UNKNOWN_TASK_TYPE.toString()
                    );
                }
            } else {
                throw new TaskFormatException();
            }
        } catch (TaskFormatException e) {
            throw e;
        }

        assert taskType != null;
        assert taskData != null;

        return new String[] { taskType, taskData, isDone };
    }
}
