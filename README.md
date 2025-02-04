# About project

This repository is a fork of the [original Duke project template](https://github.com/nus-cs2103-AY2425S2/ip) for CS2103T and has been modified to be called Bibo.
This README has been updated accordingly to reflect the changes made to the project.

## Setting up in Intellij

Prerequisites: JDK 17, update Intellij to the most recent version.

1. Open Intellij (if you are not in the welcome screen, click `File` > `Close Project` to close the existing project first)
1. Open the project into Intellij as follows:
   1. Click `Open`.
   1. Select the project directory, and click `OK`.
   1. If there are any further prompts, accept the defaults.
1. Configure the project to use **JDK 17** (not other versions) as explained in [here](https://www.jetbrains.com/help/idea/sdk.html#set-up-jdk).<br>
   In the same dialog, set the **Project language level** field to the `SDK default` option.
1. After that, locate the `src/main/java/Bibo.java` file, right-click it, and choose `Run Bibo.main()` (if the code editor is showing compile errors, try restarting the IDE). If the setup is correct, you should see the [expected output](#expected-output).

**Warning:** Keep the `src\main\java` folder as the root folder for Java files (i.e., don't rename those folders or move Java files to another folder outside of this folder path), as this is the default location some tools (e.g., Gradle) expect to find Java files.

`runtest.bat` and `runtest.sh` have been modified to run the tests for the project.

## Installation

1. Save the jar file to an empty folder.
2. Open a command window in that folder.
3. Run the command java -jar bibo.jar

If the installation is successful, you should see the [expected output](#expected-output).

## Features
* `list`: Lists all tasks in the todo list.
* `todo <description>`: Adds a todo task with the given description.
* `deadline <description> /by <date/time>`: Adds a deadline task with the given description and date.
* `event <description> /from <date/time> /to <date/time>`: Adds an event task with the given description and date.
* `mark <task number>`: Marks the task with the given task number as done.
* `unmark <task number>`: Marks the task with the given task number as not done.
* `delete <task number>`: Deletes the task with the given task number.
* `find <keyword>`: Lists all tasks with the given keyword in the description.

All date/time arguments must be in the format `<day>-<month>-<year> <hour><minute>` in numbers.

# Expected Output
```
Loading todo list...
Saved data not found. Creating new file to store data...

---------- Bibo says: ----------
Hello! I'm Bibo. What can I do for you today?

----------- You say: -----------
```
*Line 2 will differ if there exists a file `<project root>/data/bibo.txt`.*