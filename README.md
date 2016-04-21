# Nopol Dataset

This data set is a supplemental material of our project NOPOL.

Nopol in GitHub, <https://github.com/SpoonLabs/nopol/>

Nopol paper, <http://hal.archives-ouvertes.fr/hal-01285008/>

## Reproduce

To run Nopol, you need Java and JavaC version 7 and the Z3 SMT solver.
We use java version "1.7.0_79".
We use Z3 4.3.3, http://github.com/Z3Prover/z3/releases/tag/z3-4.3.3
Note that changing to another Java or Z3 version may lead to different patches from the ones in our paper. 

```bash
git clone https://github.com/SpoonLabs/nopol-experiments.git
cd nopol-experiments
# check that java is java 7
java -version # should be java 1.7"
javac -version # should be java 1.7"
python src/reproduce.py -bug cm1
```

This outputs a long line, ended by 

```
1 patches found
cm1: org.apache.commons.math.stat.univariate.rank.Percentile:148: CONDITIONAL (length)<=(fpos)
```

The other possible bugs identifiers are `cl1  cl2  cl3  cl4  cl5  cl6  cm1  cm10  cm2  cm3  cm4	cm5  cm6  cm7  cm8  cm9  pl1  pl2  pl3	pl4  pm1  pm2`.


You can also run them all at once:
```bash
python src/reproduce.py -bug all
```

By default, z3 is taken at `./data/lib/z3` (version 4.3.3, 64 bits). If you'd like to use another one:
```bash
python src/reproduce.py [-solver <path_to_z3>] -bug [bug_id|all]
```

## Nopol version in our paper

The Nopol version in our paper is as follows. 
http://github.com/SpoonLabs/nopol/blob/master/nopol/nopol-0.0.3-SNAPSHOT-jar-with-dependencies.jar

 
## Test cases in use for Nopol

For technical reasons, we have not used whole test suites for several bugs. 
Here is a list of bugs, on which only specific test classes are used. For the  bugs that are not listed here, we use the whole test suite in Nopol. The first column is the bug index; the second column is the representative test class in use (all test cases in this test class are used by Nopol). 

| Bug  | Test name                                                          |
|------|--------------------------------------------------------------------|
| CM10 | org.apache.commons.math3.stat.correlation.CovarianceTest           |
| CL1  | org.apache.commons.lang.StringUtilsTest                            |
| CL2  | org.apache.commons.lang.StringUtilsTest                            |
| CL3  | org.apache.commons.lang.StringUtilsSubstringTest                   |
| CL4  | org.apache.commons.lang.text.StrBuilderTest                        |
| CL6  | org.apache.commons.lang3.StringUtilsIsTest                         |
| PL1  | org.apache.commons.lang.time.StopWatchTest                         |
| PL2  | org.apache.commons.lang.StringEscapeUtilsTest                      | 
| PL3  | org.apache.commons.lang.WordUtilsTest                              |
| PL4  | org.apache.commons.lang3.text.translate.NumericEntityUnescaperTest |