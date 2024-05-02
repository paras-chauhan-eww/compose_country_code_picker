### Setup

#### Add this in your root `build.gradle` file (**not** your module `build.gradle` file):

```groovy
allprojects {
    repositories {
        maven { url "https://jitpack.io" }
    }
}
```

### Dependency

Add the following to your `build.gradle` dependencies:

```groovy
dependencies {
    implementation ("com.github.paras-chauhan-eww:compose_country_code_picker:<version>")
}
```

