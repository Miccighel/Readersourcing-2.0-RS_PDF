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

This is the official repository of **RS_PDF**, which is part of the **Readersourcing 2.0** ecosystem. This repository is a [Git Submodule](https://git-scm.com/book/it/v2/Git-Tools-Submodules) of the main project, which can be found by taking advantage of the links below.

<h1>Useful Links</h1>

- <a href="https://readersourcing.org">Readersourcing 2.0 (Web Interface)</a>
- <a href="https://github.com/Miccighel/Readersourcing-2.0">Readersourcing 2.0 (GitHub)</a>
- <a href="https://zenodo.org/record/1446468">Original Article</a>
- <a href="https://zenodo.org/record/1452397">Technical Documentation (Zenodo)</a>
- <a href="https://github.com/Miccighel/Readersourcing-2.0-TechnicalDocumentation"> Technical Documentation (GitHub)</a>
- <a href="https://doi.org/10.5281/zenodo.1442597">Zenodo Record</a>

<h1>Description</h1>

**RS_PDF** is the software library utilized by [RS_Server](https://github.com/Miccighel/Readersourcing-2.0-RS_Server) to edit the PDF files and add
the required URL when a reader requests to save a publication for later reading. 

It is characterized by a command-line interface, allowing RS_Server to use it directly. Since they are deployed together, they can communicate without the need for complex channels and paradigms.

<h1>Installation</h1>

RS_PDF can be bundled with a compatible RS_Server distribution, eliminating the need for manual installation when deploying the latter. However, if you wish to use RS_PDF
independently, you can download the shaded `.jar` file from the release section of this repository and place it anywhere on your filesystem.

To build and verify RS_PDF from source, run `./mvnw clean verify` (`mvnw.cmd clean verify` on Windows). The Maven Wrapper downloads the configured Maven version when necessary.
The executable file is generated at `target/RS_PDF-v2.0.0-shaded.jar`.

<h1>Requirements</h1>

- <a href="https://www.oracle.com/java/technologies/downloads/#java21">JDK (Java Development Kit)</a> >= 21.

<h1>Command Line Interface (CLI)</h1>

The behavior of RS_PDF is configured during its startup phase by RS_Server through a set of special command-line options. For this reason,
it is useful to provide a list of all the options that can be used if it is necessary to employ RS_PDF in other contexts, modify its implementation,
or for any other reason. Input and output paths use their default configuration when both options are omitted, while caption and URL are always required. The list of command-line options
is shown in the following table:

| Short | Long | Description | Values | Required | Depends On |
|-------|------|-------------|--------|----------|------------|
| `-pIn` | `--pathIn` | Path on the filesystem from which to load the PDF files to be edited. It can be a file or a folder. | String representing a relative or absolute path. | No | `-pOut` |
| `-pOut` | `--pathOut` | Path on the filesystem in which to save the edited PDF files. It must be a folder. | String representing a relative or absolute path. | No | `-pIn` |
| `-c` | `--caption` | Caption of the link to add. | Any string. | Yes | No |
| `-u` | `--url` | URL to add. | A valid HTTP or HTTPS URL without embedded credentials. | Yes | No |
| `-a` | `--authToken` | Authentication token received from RS_Server. | A valid authentication token received from RS_Server. | No | `-pOut -pIn -pId` |
| `-pId` | `--publicationId` | Identifier for a publication present on RS_Server. | A valid publication identifier received from RS_Server. | No | `-pOut -pIn -a` |

To provide an execution example, let's assume a scenario where there is a need to edit some files encoded in PDF format with the following prerequisites:

- There is a folder containing `n` files to edit at the path `C:\data`.
- The edited files must be saved inside a folder at the path `C:\out`.
- The file in JAR format containing the library is called `RS_PDF-v2.0.0-shaded.jar`.
- The JAR file containing RS_PDF is located inside the folder at the path `C:\lib`.

The execution of RS_PDF is initiated with the command:

`java -jar C:\lib\RS_PDF-v2.0.0-shaded.jar -pIn C:\data -pOut C:\out -c "Express your rating" -u "https://readersourcing.example/rate"`.

RS_PDF returns a non-zero process exit code when its parameters are invalid or
when a publication cannot be read or written. Authentication tokens and full
rating URLs are deliberately omitted from operational logs.
