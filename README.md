If javac warnings are treated as errors, you cannot compile if the schema has custom types.

```
SimpleCustomTypeWithLintsWarningsAsErrors git:(master) ✗  JAVA_HOME=$(/usr/libexec/java_home -v 1.8) ./gradlew clean build -PWARNINGS_AS_ERRORS=1

> Configure project :app 
The CompileOptions.bootClasspath property has been deprecated and is scheduled to be removed in Gradle 5.0. Please use the CompileOptions.bootstrapClasspath property instead.

> Task :app:compileDebugJavaWithJavac FAILED
/Users/cdawson/Projects/SimpleCustomTypeWithLintsWarningsAsErrors/app/src/main/java/com/webiphany/simplecustomtypewithlintswarningsaserrors/MainActivity.java:55: warning: [rawtypes] found raw type: CustomTypeValue
            public String decode(CustomTypeValue value) {
                                 ^
  missing type arguments for generic class CustomTypeValue<T>
  where T is a type-variable:
    T extends Object declared in class CustomTypeValue
error: warnings found and -Werror specified
/Users/cdawson/Projects/SimpleCustomTypeWithLintsWarningsAsErrors/app/build/generated/source/apollo/com/webiphany/type/CustomType.java:18: warning: [rawtypes] found raw type: Class
    public Class javaType() {
           ^
  missing type arguments for generic class Class<T>
  where T is a type-variable:
    T extends Object declared in class Class
/Users/cdawson/Projects/SimpleCustomTypeWithLintsWarningsAsErrors/app/build/generated/source/apollo/com/webiphany/type/CustomType.java:30: warning: [rawtypes] found raw type: Class
    public Class javaType() {
           ^
  missing type arguments for generic class Class<T>
  where T is a type-variable:
    T extends Object declared in class Class
1 error
3 warnings


FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:compileDebugJavaWithJavac'.
> Compilation failed; see the compiler error output for details.

* Try:
Run with --stacktrace option to get the stack trace. Run with --info or --debug option to get more log output. Run with --scan to get full insights.

* Get more help at https://help.gradle.org

BUILD FAILED in 14s
20 actionable tasks: 18 executed, 2 up-to-date
➜  SimpleCustomTypeWithLintsWarningsAsErrors git:(master) ✗ 


```

Running without the flag shows no errors:

```
➜  SimpleCustomTypeWithLintsWarningsAsErrors git:(master) ✗  JAVA_HOME=$(/usr/libexec/java_home -v 1.8) ./gradlew clean build

> Configure project :app
The CompileOptions.bootClasspath property has been deprecated and is scheduled to be removed in Gradle 5.0. Please use the CompileOptions.bootstrapClasspath property instead.

> Task :app:lint
Ran lint on variant release: 12 issues found
Ran lint on variant debug: 12 issues found
Wrote HTML report to file:///Users/cdawson/Projects/SimpleCustomTypeWithLintsWarningsAsErrors/app/build/reports/lint-results.html
Wrote XML report to file:///Users/cdawson/Projects/SimpleCustomTypeWithLintsWarningsAsErrors/app/build/reports/lint-results.xml

BUILD SUCCESSFUL in 27s
65 actionable tasks: 62 executed, 3 up-to-date
```