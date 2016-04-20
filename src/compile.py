# /bin/python
import argparse
import os

import Bug
import Project
from Config import conf


def initParser():
    parser = argparse.ArgumentParser(description='Compile data set')
    parser.add_argument('-dataset', default=conf.pathDataset,
                        help='The location of the dataset')
    parser.add_argument('-projects', nargs='+', required=True,
                        help='Which project (math, lang)')
    parser.add_argument('-id', nargs='+', help='Bug id')
    return parser.parse_args()


args = initParser()

for projectName in args.projects:
    for id in args.id:
        project = Project.Project(projectName)
        bug = Bug.Bug(project, id)
        bug.compile(args.dataset)
