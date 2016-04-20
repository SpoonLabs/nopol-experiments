# /bin/python

import subprocess


class Repository(object):
    """docstring for Repository"""

    def __init__(self, path):
        super(Repository, self).__init__()
        self.path = path

    def getParent(self, commitId):
        cmd = """cd %s
		git rev-list --parents -n 1 %s""" % (self.path, commitId)
        output = subprocess.check_output(cmd, shell=True)
        return output.replace("%s " % commitId, "")
