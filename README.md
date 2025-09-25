# `@AggregateRepeatedTest` JUnit Annotation

> [!WARNING]
> This is not a production-ready package.
> It is used mostly for our internal experiments and research.
> Use it at your own risk.

The `@AggregateRepeatedTest` annotation is a custom JUnit annotation
that allows you to run a test method multiple times.
You just replace your `@Test` annotation with `@AggregateRepeatedTest`
and specify the number of repetitions as an argument.
The test method will be executed the specified number of times.
For example, the following test method will be executed 10 times:

```java
import com.yegor256.AggregateRepeatedTest;
@AggregateRepeatedTest(10)
void testSomething() {
    // test code here
}
```

Each test run is logged to the `/tmp/aggregate-repeated-test.txt` file.
The location can't be changed.
The file is appended.
