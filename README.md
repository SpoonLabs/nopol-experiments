# Nopol Dataset

This data set is a supplemental material of our project NOPOL.

Nopol in GitHub, https://github.com/SpoonLabs/nopol/

Nopol paper, http://hal.archives-ouvertes.fr/hal-01285008/

The latest version of Nopol as well as its usage can be found in https://github.com/SpoonLabs/nopol/ 

## Reproduce

To run Nopol, Z3 SMT solver is a must. 
We recommend Z3 4.3.2, http://github.com/Z3Prover/z3/releases/tag/z3-4.3.2 
Note that changing to another version may lead to different patches from the ones in our paper. 

```bash
git clone https://github.com/SpoonLabs/nopol-experiments.git
cd nopol-experiments
python src/reproduce.py [-solver <path_to_z3>] -bug [bug_id|all]
```

## Nopol version in our paper
We update Nopol by adding new features and correcting existing bugs. 

The Nopol version in our paper is as follows. 
http://github.com/SpoonLabs/nopol/blob/master/nopol/nopol-0.0.3-SNAPSHOT-jar-with-dependencies.jar

To run Nopol, Z3 SMT solver is a must. 
We recommend Z3 4.3.2, http://github.com/Z3Prover/z3/releases/tag/z3-4.3.2 
Note that changing to another version may lead to different patches from the ones in our paper. 

The following command will show the usage of Nopol. 
```java -jar nopol-0.0.3-SNAPSHOT-jar-with-dependencies.jar``` 
 
## Test cases in use for Nopol

For technical reasons, we have not used whole test suites for several bugs. 
Here is a list of bugs, on which only representative test classes are used. For bugs that are not listed here, we use the whole test suite in Nopol. The first column is the bug index; the second column is the representative test class in use (test cases in this test class are used by Nopol). 

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