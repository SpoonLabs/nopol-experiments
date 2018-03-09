# /bin/python
import argparse
import os
import re
import shutil
import subprocess
import sys

import Bug
import Project
from Config import conf


class bcolors:
    HEADER = '\033[95m'
    OKBLUE = '\033[94m'
    OKGREEN = '\033[92m'
    WARNING = '\033[93m'
    FAIL = '\033[91m'
    ENDC = '\033[0m'
    BOLD = '\033[1m'
    UNDERLINE = '\033[4m'


def colored(text, colors):
    return "%s%s%s" % ("".join(colors), text, bcolors.ENDC)


def execute(bug, solverPath):
    pathBug = os.path.join(conf.pathDataset, bug.id)
    if os.path.isdir(os.path.join(pathBug, "target")):
        shutil.rmtree(os.path.join(pathBug, "target"))
    # compile the bug
    print colored("Compiling the bug %s" % bug.id, [bcolors.BOLD])
    bug.compile(conf.pathDataset)
    print colored("Repairing the bug %s" % bug.id, [bcolors.BOLD])
    tests = ""
    if 'tests' in bug.data:
        for test in bug.data['tests']:
            tests += '"' + test + '"' + " "
    source = os.path.join(pathBug, bug.data['path']['source'])
    # java -jar <nopol-jar-file> nopol <source path> <class path> z3|cvc4 <solver-path> [<test-class-name>...]
    cmd = """
./call_nopol.sh %s %s %s %s
""" % (
        source, # $1 source folder
        bug.getClasspath(conf.pathDataset),  # $1 concatenated classpath folder
        solverPath, # $3 solverpath
        tests # $4 concatenated tests to be executed
        )
    process = subprocess.Popen(cmd, shell=True, stdout=subprocess.PIPE,
                               stderr=subprocess.STDOUT)
    patchFound = False
    patch = ""
    while True:
        nextline = process.stdout.readline()
        if nextline == '' and process.poll() != None:
            break
        if patchFound:
            patch = patch + nextline
        if '----PATCH FOUND----' in nextline:
            patchFound = True
            nextline = colored(nextline, [bcolors.OKGREEN])
        m = re.search(
            '(([0-9]+-[0-9]+-[0-9]+ )?[0-9]+:[0-9]+:[0-9]+(,|\.)[0-9]+) (\[([^\]]+)\] )?([^ ]+) ([^-]+) - (.*)',
            nextline)
        if m:
            nextline = "%s %s - %s\n" % (
                #colored(m.group(1), [bcolors.HEADER]),
                #colored(m.group(5), [bcolors.WARNING]),
                colored(m.group(6), [bcolors.OKBLUE]),
                colored(m.group(7), [bcolors.UNDERLINE]),
                m.group(8))
        if not conf.silence:
            sys.stdout.write(nextline)
            sys.stdout.flush()
    if patch != "":
        print colored("%s: %s" % (bug.id, patch), [bcolors.OKGREEN, bcolors.BOLD])
    else:
        print colored("No patch for %s" % (bug.id), [bcolors.FAIL, bcolors.BOLD])
    return patch


def bugIdToBug(bugId):
    bugId = bugId.lower()
    project = None
    if bugId[1] == "m":
        project = Project.Project("math")
    else:
        project = Project.Project("lang")
    return Bug.Bug(project, bugId)


def getArgs():
    parser = argparse.ArgumentParser(description='Initialize the dataset')
    parser.add_argument('-bug', help='The bug to execute')
    parser.add_argument('-solver', default=os.path.join(conf.pathLib, "z3"),
                        help='The path to the z3 solver')
    parser.add_argument('--silence', default=False, action='store_true', help='Silence execution')
    return parser.parse_args()


if __name__ == '__main__':
    args = getArgs()
    patches = {}
    conf.silence = args.silence
    if args.bug.lower() == "all":
        for bug in ["cm1", "cm2", "cm3", "cm4", "cm5", "cm6", "cm7", "cm10",
                    "cl1", "cl2", "cl3", "cl4", "cl5", "pm1", "pm2", "pl1",
                    "pl2", "pl4"]:
            patch = execute(bugIdToBug(bug), args.solver)
            if patch != "":
                patches[bug] = patch
    else:
        patch = execute(bugIdToBug(args.bug), args.solver)
        if patch != "":
            patches[args.bug] = patch

    print("\n\n%d patches found" % len(patches))
    for bug in sorted(patches):
        print colored("%s: %s" % (bug, patches[bug]), [bcolors.OKGREEN, bcolors.BOLD])
    if len(patches) == 0: sys.exit(-1)
