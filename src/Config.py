import os


class Config(object):
    def __init__(self):
        self.root = os.path.join(os.path.dirname(os.path.realpath(__file__)),
                                 "..")
        self.pathDataset = os.path.join(self.root, "dataset")
        self.pathScript = os.path.join(self.root, "src")
        self.pathData = os.path.join(self.root, "data")
        self.pathLib = os.path.join(self.pathData, "lib")
        self.pathProjectData = os.path.join(self.pathData, "projects")
        self.resultsFolder = os.path.join(
            os.path.dirname(os.path.realpath(__file__)), "..", "results")
        self.silence = False


conf = Config()
