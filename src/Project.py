# /bin/python

import glob
import json
import os
import subprocess

from Config import conf


class Project(object):
    """docstring for Repository"""

    def __init__(self, name):
        super(Project, self).__init__()
        self.name = name
        path = os.path.join(conf.pathProjectData, name.lower(), "project.json")
        with open(path) as data_file:
            self.data = json.load(data_file)
