# /bin/python

import os
import subprocess

import Bug
import Repository
from Config import conf

for id in xrange(1, 11):
    id = "cm%d" % id
    patchPath = os.path.join(conf.pathProjectData, "math/patch", id)
    bug = Bug.Bug("math", id)
    cmd = """cd %s
git diff %s %s %s > %s.source.diff
git diff %s %s %s > %s.test.diff
git checkout -- .
git checkout %s
git apply %s.test.diff
git apply %s.transformation_source.diff
git apply %s.transformation_test.diff
git apply %s.additional_test.diff
""" % (bug.path,
       bug.data['commit']['buggy'],
       bug.data['commit']['patch'],
       bug.data['path']['source'],
       patchPath,
       bug.data['commit']['buggy'],
       bug.data['commit']['patch'],
       bug.data['path']['test'],
       patchPath,
       bug.data['commit']['buggy'],
       patchPath,
       patchPath,
       patchPath,
       patchPath)
    subprocess.call(cmd, shell=True)
    # bug.compile()
