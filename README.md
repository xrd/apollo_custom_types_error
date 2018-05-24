## TL;DR

Running this command produces errors 

```
JAVA_HOME=$(/usr/libexec/java_home -v 1.8) ./gradlew clean build -PWARNINGS_AS_ERRORS=1
```

ERROR: (`warning: [rawtypes] found raw type: CustomTypeValue ... missing type arguments for generic class Class<T>`):

This command (without `-PWARNINGS_AS_ERRORS=1` added) produces no errors:

```
JAVA_HOME=$(/usr/libexec/java_home -v 1.8) ./gradlew clean build
```

## Summary

This repository shows how a custom scalar type is incompatible with warnings as errors for javac.

The custom type is defined [here](https://github.com/xrd/apollo_custom_types_error/blob/master/server/app.js#L13):
```
// The GraphQL schema in string form
const typeDefs = `
  type Query { links: [URL] }
  scalar URL
`;
```

Schema was generated using `apollo-codegen` (`npm install apollo-codegen`). Server app is in [server](server) and can be started with `npm run start`. Then, use the following command to pull the schema.json which can be loaded into the Android project.

```
apollo-codegen download-schema \
  http://localhost:8080/graphql \
  --output ./app/src/main/graphql/com/webiphany/schema.json
```

The generated custom type looks like this:

```
@Generated("Apollo GraphQL")
public enum CustomType implements ScalarType {
  URL {
    @Override
    public String typeName() {
      return "URL";
    }

    @Override
    public Class javaType() {
      return String.class;
    }
  },

  ID {
    @Override
    public String typeName() {
      return "ID";
    }

    @Override
    public Class javaType() {
      return String.class;
    }
  }
}

```

[Using it looks like this](https://github.com/xrd/apollo_custom_types_error/blob/master/app/src/main/java/com/webiphany/simplecustomtypewithlintswarningsaserrors/MainActivity.java)

```
CustomTypeAdapter<String> customTypeAdapter = new CustomTypeAdapter<String>() {
            @Override
            public String decode(CustomTypeValue value) {
                return value.value.toString();
            }

            @Override
            public CustomTypeValue<?> encode(String value) {
                return new CustomTypeValue.GraphQLString(value);
            }
        };

        ApolloClient apolloClient = ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.URL, customTypeAdapter)
                .build();
```

## Errors

If javac warnings are treated as errors, you cannot compile if the schema has custom types.

```
$ JAVA_HOME=$(/usr/libexec/java_home -v 1.8) ./gradlew clean build -PWARNINGS_AS_ERRORS=1

> Configure project :app 
The CompileOptions.bootClasspath property has been deprecated and is scheduled to be removed in Gradle 5.0. Please use the CompileOptions.bootstrapClasspath property instead.

> Task :app:compileDebugJavaWithJavac FAILED
/Users/cdawson/Projects/SimpleCustomTypeWithLintsWarningsAsErrors/app/src/main/java/com/webiphany/simplecustomtypewithlintswarningsaserrors/MainActivity.java:56: warning: [rawtypes] found raw type: CustomTypeValue
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

BUILD FAILED in 10s
20 actionable tasks: 18 executed, 2 up-to-date

```

## Regular javac

Running without the flag shows no errors:

```
$ JAVA_HOME=$(/usr/libexec/java_home -v 1.8) ./gradlew clean build

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
