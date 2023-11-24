[![Maintenance](https://img.shields.io/badge/Maintained%3F-yes-green.svg)](https://GitHub.com/Naereen/StrapDown.js/graphs/commit-activity)
![Maintainer](https://img.shields.io/badge/maintainer-Miccighel-blue)
[![Github all releases](https://img.shields.io/github/downloads/Miccighel/Readersourcing-2.0-RS_PDF/total.svg)](https://GitHub.com/Miccighel/Readersourcing-2.0-RS_PDF/releases/)
[![GitHub stars](https://badgen.net/github/stars/Miccighel/Readersourcing-2.0-RS_PDF)](https://GitHub.com/Miccighel/Readersourcing-2.0-RS_PDF/stargazers/)
[![GitHub watchers](https://badgen.net/github/watchers/Miccighel/Readersourcing-2.0-RS_PDF/)](https://GitHub.com/Miccighel/Readersourcing-2.0-RS_PDF/watchers/)
[![GitHub contributors](https://img.shields.io/github/contributors/Miccighel/Readersourcing-2.0-RS_PDF.svg)](https://GitHub.com/Miccighel/Readersourcing-2.0-RS_PDF/graphs/contributors/)
[![GitHub issues](https://img.shields.io/github/issues/Miccighel/Readersourcing-2.0-RS_PDF.svg)](https://GitHub.com/Miccighel/Readersourcing-2.0-RS_PDF/issues/)
[![GitHub issues-closed](https://img.shields.io/github/issues-closed/Miccighel/Readersourcing-2.0-RS_PDF.svg)](https://GitHub.com/Miccighel/Readersourcing-2.0-RS_PDF/issues?q=is%3Aissue+is%3Aclosed)
[![GitHub pull-requests](https://img.shields.io/github/issues-pr/Miccighel/Readersourcing-2.0-RS_PDF.svg)](https://GitHub.com/Miccighel/Readersourcing-2.0-RS_PDF/pull/)
[![GitHub pull-requests closed](https://img.shields.io/github/issues-pr-closed/Miccighel/Readersourcing-2.0-RS_PDF.svg)](https://GitHub.com/Miccighel/Readersourcing-2.0-RS_PDF/pull/)
[![PRs Welcome](https://img.shields.io/badge/PRs-welcome-brightgreen.svg?style=flat-square)](http://makeapullrequest.com)

Readersourcing 2.0: _An independent, third-party, no-profit, academic project aimed at quality rating of scholarly literature and scholars._


<h1>Info</h1>

This is the official repository of **RS_PDF**, which is part of the **Readersourcing 2.0** ecosystem. This repository is a <a href="https://git-scm.com/book/it/v2/Git-Tools-Submodules">Git Submodule</a> of the main project which can be found by taking advantage of the links below. 

<h1>Read this!</h1>

Please, note that this is an early alpha release and it is not ready for the use in a production environment.

<h1>Useful Links</h1>

- <a href="https://readersourcing.org">Readersourcing 2.0 (Web Interface)</a>
- <a href="https://github.com/Miccighel/Readersourcing-2.0">Readersourcing 2.0 (GitHub)</a>
- <a href="https://zenodo.org/record/1446468">Original Article</a>
- <a href="https://zenodo.org/record/1452397">Technical Documentation (Zenodo)</a>
- <a href="https://github.com/Miccighel/Readersourcing-2.0-TechnicalDocumentation"> Technical Documentation (GitHub)</a>
- <a href="https://doi.org/10.5281/zenodo.1442597">Zenodo Record</a>

<h1>Description</h1>

**RS_PDF** is the software library which is exploited by <a href="https://github.com/Miccighel/Readersourcing-2.0-RS_Server">RS_Server</a> to actually edit the PDF files to add the URL required when a reader requests to save for later the publication that he is reading. It is a software characterized by a command line interface and this means that RS_Server can use it directly since they are deployed one along the other, without using complex communication channels and paradigms.

<h1>Installation</h1>

RS_PDF comes bundled with RS_Server, so when the latter is deployed there is no need to manually install the former. Nevertheless, it is possible to use it independently; it is sufficient to download the attached .jar files from the release section of this repository and place it somewhere on your filesystem. 

<h1>Requirements</h1>

 - <a href="https://www.java.com/it/download/">JRE (Java Runtime Environment)</a> >= 1.8.0;

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

To provide an execution example, let's assume a scenario in which there is the need of edit some files encoded in PDF format with the following prerequisites:
- there is a folder containing `n` files to edit at path ```C:\data```;
- the edited files must be saved inside a folder at path ```C:\out```;
- the file in JAR format containing the library is called ```RS_PDF-v1.0-alpha.jar```;
- the JAR file containing RS_PDF is located inside the folder at path ```C:\lib```;
The execution of RS\_PDF is started with the command: ```java -jar C:\lib\RS_PDF-v1.0-alpha.jar -pIn C:\data -pOut C:\out```.
