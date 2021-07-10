# Zip Archiver

# General Info 
This is a zip archiver created in Java. It allows the user to perform the following operations: 
* Create a new archive
* Add an entry to an archive
* Remove an entry from an archive
* Extract entries from an archive
* View the contents of an archive
* Delete an archive 

# Screenshots 

![Zip Archiver](https://user-images.githubusercontent.com/49923044/124396802-d08f5380-dcd9-11eb-808d-db34fbcc4023.jpg)

![Screenshot from 2021-06-23 16-21-43](https://user-images.githubusercontent.com/49923044/123165900-cc924480-d442-11eb-8767-a86a84af1140.png)

![Screenshot from 2021-06-23 16-41-43](https://user-images.githubusercontent.com/49923044/123165920-d1ef8f00-d442-11eb-8e48-71ae08c32874.png)

![Screenshot from 2021-06-23 16-44-27](https://user-images.githubusercontent.com/49923044/123165938-d6b44300-d442-11eb-9ae8-6f9e9854ba16.png)

![Screenshot from 2021-06-23 16-45-35](https://user-images.githubusercontent.com/49923044/123165949-d87e0680-d442-11eb-90e8-387b806f37d0.png)

![Screenshot from 2021-06-23 16-46-22](https://user-images.githubusercontent.com/49923044/123165956-dae06080-d442-11eb-8c84-1c4d38e1f0c7.png)

![Screenshot from 2021-06-23 16-47-03](https://user-images.githubusercontent.com/49923044/123165961-dcaa2400-d442-11eb-9e21-98a2d5d499c5.png)

![Screenshot from 2021-06-23 16-47-22](https://user-images.githubusercontent.com/49923044/123165980-dfa51480-d442-11eb-8681-b1304e7491f0.png)

# Technologies
1) Java programming language
3) Command design pattern

# Setup

# Features
* Archiver
  * Main class which prints a help menu and prompts the user to select an operation
* Console Helper
  * Allows the user to interact with the console
  * Reads input from the console
  * Ouputs text to the console
* Command Executor
  * Statically initializes a map with all the commands keyed by their respective operations that a user may want to perform on an archive
  * Allows the program to get an operation from the command map and execute it
* File Manager
  * Represents a list of files
  * Uses depth first search to collect files from the specified directory into an array list
* File Properties
  * Represents the properties of a file such as name, size, compressed size, and compression method
  * Primarily used by the ZipContentCommand class to display file properties for each file in the archive
* Zip File Manager
  * Controller class that contains the logic for each of the zip commands. For example ZipAddCommand.java adds an entry into the archive by instantiating a ZipFileManager object and calling the addFile() method. 
* Commands
  * There is a command for each user intended operation. All commands create a zip file manager and execute the appropriate method from the ZipFileManager class to perform the operation
  * Zip add command: Prompts the user for the full path of the file to be added and adds the file to the archive
  * Zip content command: Gets a list of all the files in the archive and uses the FileProperties class to output the file properties to the console
  * Zip create command: Prompts the user for the file or directory to be archived and creates a new archive at that destination
  * Zip extract command: Prompts the user for the path where the archive will be unpacked and extracts the contents of the archive into the destination
  * Zip remove command: Prompts the user for the full path of the file to be removed from the archive and removes the file from the archive
  * Zip exit command: Exits the program

# Status
Completed

# Inspiration
In the past I always had to download a zip archiver tool to extract a zip archive. Today it's built into the operating system. This prompted me to learn how a zip archiver worked by creating my own.
