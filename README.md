<h1>Read this!</h1>

Please, note that this is an early alpha release and it is not ready for the use in a production environment.

<h1>Useful Links</h1>

- <a href="https://zenodo.org/record/1446468">Original Article</a>
- <a href="https://zenodo.org/record/1452397">Technical Documentation</a>

<h1>Description</h1>

RS_PDF is the software library which is exploited by <a href="https://github.com/Miccighel/Readersourcing-2.0-RS_Server">RS_Server</a> to actually edit the PDF files to add the URL required when a reader requests to save for later the publication that he is reading. It is a software characterized by a command line interface and this means that RS_Server can use it directly since they are deployed one along the other, without using complex communication channels and paradigms.

<h1>Installation</h1>

RS_PDF comes bundled with RS_Server, so when the latter is deployed there is no need to manually install the former. Nevertheless, it is possible to use it independently; it is sufficient to download the attached .jar files from the release section of this repository and place it somewhere on your filesystem. 

<h1>Requirements</h1>

 - <a href="https://www.java.com/it/download/">JRE (Java Runtime Environment)</a> >= 1.8.0;

<h1>Usage</h1>

The usage of RS_PDF is quite simple. To provide an execution example, let's assume a scenario in which there is the need of edit some files encoded in PDF format with the following prerequisites:
- there is a folder containing `n` files to edit at path ```C:\data```;
- the edited files must be saved inside a folder at path ```C:\out```;
- the file in JAR format containing the library is called ```RS_PDF-v1.0-alpha.jar```;
- the JAR file containing RS_PDF is located inside the folder at path ```C:\lib```;
The execution of RS\_PDF is started with the command: ```java -jar C:\lib\RS_PDF-v1.0-alpha.jar -pIn C:\data -pOut C:\out```.

<h1>Command Line Interface (CLI)</h1>

The behavior of RS_PDF is configured during its startup phase by RS_Server through a set of special command-line options. For this reason, it is useful to provide a list of all the options that can be used if it is necessary to use RS_PDF in other contexts, modify its implementation or for any other reason. However, it is designed to work with a default configuration if no options are provided. This list of command line options in shown in the following table:

| Short | Long | Description | Values | Required | Dependencies |
| ------------- | ------------- | ------------- | ------------- | ------------- | ------------- |
| ```--pIn``` | ```--pathIn``` | Path on the filesystem from which to load the PDF files to be edited. It can be a file or a folder. | String representing a relative path. | No | ```--pOut``` |
| ```--pOut``` | ```--pathOut``` | Path on the filesystem in which to save the edited PDF files. It must be a folder. | String representing a relative path. | No | ```--pIn``` |
| ```--c``` | ```--caption``` | Caption of the link to add. | Any string. | Yes | No |
| ```--u``` | ```--url``` | Url to add. | A valid URL. | Yes | No |
| ```--a``` | ```--authToken``` | Authentication token received from RS_Server. | A valid authentication token received RS_Server. | No | ```--pOut --pIn --pId``` |
| ```--pId``` | ```--publicationId``` | Identifier for a publication present on RS_Server. | A valid publication identifier received from RS_Server. | No | ```--pOut --pIn --a``` |
