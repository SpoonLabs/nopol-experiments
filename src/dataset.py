# /bin/python
import argparse
import os
import re
import subprocess

import Bug
import Project
from Config import conf


def getM2Repository():
    m2Repo = ""
    cmd = "mvn -X; exit 0;"
    output = subprocess.check_output(cmd, shell=True)
    m = re.search("Using local repository at (.*)", output)
    return m.group(1)


def getProjects():
    output = []
    dirs = os.listdir(conf.pathProjectData)
    for projectName in dirs:
        projectPath = os.path.join(conf.pathProjectData, projectName)
        if not os.path.isdir(projectPath):
            continue
        if os.path.isfile(os.path.join(projectPath, "project.json")):
            project = Project.Project(projectName)
            output += [project]
    return output


def getBugs(projects):
    output = []
    for project in projects:
        projectPath = os.path.join(conf.pathProjectData, project.name)
        pathBugs = os.path.join(projectPath, "bugs")
        fileBugs = os.listdir(pathBugs)
        for fileBug in fileBugs:
            if not fileBug.endswith(".json"):
                continue
            pathBug = os.path.join(pathBugs, fileBug)
            if os.path.isdir(pathBug):
                continue
            bugId = fileBug.replace(".json", "")
            bug = Bug.Bug(project, bugId)
            output += [bug]
    return output


def initDataset(datasetLocation, compile):
    if not os.path.exists(datasetLocation):
        os.makedirs(datasetLocation)
    projects = getProjects()
    bugs = getBugs(projects)
    print(projects)
    for bug in bugs:
        bug.initBug(bug.project.data['repo'], datasetLocation)
        if compile:
            bug.compile(datasetLocation)
    pass


def getArgs():
    parser = argparse.ArgumentParser(description='Initialize the dataset')
    parser.add_argument('-output', default=os.path.join(conf.pathDataset),
                        help='The location of the dataset')
    parser.add_argument('-compile', nargs='?', default=True,
                        help='The compile the dataset')
    return parser.parse_args()


if __name__ == '__main__':
    args = getArgs()
    # m2Repo = getM2Repository()
    initDataset(args.output, args.compile)
