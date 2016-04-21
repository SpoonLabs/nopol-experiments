# /bin/python

import glob
import json
import os
import subprocess

from Config import conf


class Bug(object):
    """docstring for Repository"""

    def __init__(self, project, id):
        super(Bug, self).__init__()
        self.project = project
        self.id = id
        path = os.path.join(conf.pathProjectData, project.name.lower(), "bugs",
                            "%s.json" % id)
        with open(path) as data_file:
            self.data = json.load(data_file)

    def initBug(self, url, datasetLocation):
        bugLocation = os.path.join(datasetLocation, self.id)
        patchPath = os.path.join(conf.pathProjectData,
                                 self.project.name.lower(), "patch", self.id)
        cmd = """cd %s;
        git clone %s %s;
        cd %s;
        git reset --hard %s;
        rm -rf src/assembly src/site src/experimental src/mantissa;
        git apply %s.test.diff;
        git apply %s.transformation_source.diff;
        git apply %s.transformation_test.diff;
        git apply %s.additional_test.diff;
        """ % (datasetLocation,
               url,
               self.id,
               self.id,
               self.data['commit']['buggy'],
               patchPath,
               patchPath,
               patchPath,
               patchPath)
        subprocess.call(cmd, shell=True)
        pass

    def getResources(self, datasetPath):
        sourceFiles = ""
        testFiles = ""
        sourceResources = []
        testResources = []

        for root, dirs, files in os.walk(os.path.join(datasetPath, self.id,
                                                      self.data['path'][
                                                          'source'])):
            for file in files:
                if file.endswith('.java'):
                    sourceFiles += os.path.join(
                        root.replace(datasetPath + "/" + self.id + "/", ""),
                        file) + " "
                elif not file.endswith('.java') and not file.endswith('.class'):
                    sourceResources += [{
                        "path": os.path.join(root, file),
                        "name": os.path.join(root.replace(
                            datasetPath + "/" + self.id + "/" +
                            self.data['path']['source'], ""), file),
                    }]
        for root, dirs, files in os.walk(
                os.path.join(datasetPath, self.id, self.data['path']['source'],
                             "../resources")):
            for file in files:
                if file.endswith('.java'):
                    sourceFiles += os.path.join(
                        root.replace(datasetPath + "/" + self.id + "/", ""),
                        file) + " "
                elif not file.endswith('.java') and not file.endswith('.class'):
                    sourceResources += [{
                        "path": os.path.join(root, file),
                        "name": os.path.join(root.replace(
                            datasetPath + "/" + self.id + "/" +
                            self.data['path']['source'] + "../resources/", ""),
                                             file),
                    }]
        for root, dirs, files in os.walk(
                os.path.join(datasetPath, self.id, self.data['path']['test'])):
            for file in files:
                if file.endswith('.java'):
                    testFiles += os.path.join(
                        root.replace(datasetPath + "/" + self.id + "/", ""),
                        file) + " "
                elif not file.endswith('.java') and not file.endswith('.class'):
                    testResources += [{
                        "path": os.path.join(root, file),
                        "name": os.path.join(root.replace(
                            datasetPath + "/" + self.id + "/" +
                            self.data['path']['test'], ""), file),
                    }]
        for root, dirs, files in os.walk(
                os.path.join(datasetPath, self.id, self.data['path']['test'],
                             "../resources")):
            for file in files:
                if file.endswith('.java'):
                    testFiles += os.path.join(
                        root.replace(datasetPath + "/" + self.id + "/", ""),
                        file) + " "
                elif not file.endswith('.java') and not file.endswith('.class'):
                    testResources += [{
                        "path": os.path.join(root, file),
                        "name": os.path.join(root.replace(
                            datasetPath + "/" + self.id + "/" +
                            self.data['path']['test'] + "../resources/", ""),
                                             file),
                    }]
        return (sourceFiles, testFiles, sourceResources, testResources)

    def getClasspath(self, datasetPath):
        classpath = os.path.join(datasetPath, self.id, "target", "classes")
        classpath += ":" + os.path.join(datasetPath, self.id, "target",
                                        "test-classes")
        for dep in self.data['dependencies']:
            classpath += ":" + os.path.join(conf.pathLib, dep)
        return classpath

    def getJavaVersion(self):
        javaVersion = "1.7"
        if ("java" in self.data and "version" in self.data["java"]):
            javaVersion = self.data["java"]["version"]
        return javaVersion

    def compile(self, datasetPath):
        classpath = self.getClasspath(datasetPath)
        javaVersion = self.getJavaVersion()

        (sourceFiles, testFiles, sourceResources, testResources) = self.getResources(datasetPath)
        cmdRessources = ""
        for resource in sourceResources:
            cmdRessources += """mkdir -p target/classes/%s
cp %s target/classes/%s
""" % (os.path.dirname(resource['name']), resource['path'], resource['name'])
        for resource in testResources:
            cmdRessources += """mkdir -p target/test-classes/%s
cp %s target/test-classes/%s
""" % (os.path.dirname(resource['name']), resource['path'], resource['name'])
        cmd = """cd %s
rm -rf target
mkdir target
mkdir target/classes
mkdir target/test-classes
%s
javac -source %s -d target/classes -g -encoding cp1252 -classpath %s %s""" % (
        os.path.join(datasetPath, self.id), cmdRessources, javaVersion,
        classpath, sourceFiles)
        response = subprocess.call(cmd, shell=True)
        if response != 0:
            print "Compilation error: %s" % self.id
            return
        cmd = """cd %s
javac -source %s -d target/test-classes -g -encoding cp1252 -classpath %s %s""" % (
        os.path.join(datasetPath, self.id), javaVersion, classpath, testFiles)
        subprocess.call(cmd, shell=True)
