# Changes For Build Project

## Changes for `gradle/wrapper/gradle-wrapper.properties`

修改 distributionUrl 为以下值：

```properties
distributionUrl=https\://mirrors.cloud.tencent.com/gradle/gradle-7.6.4-bin.zip
```

## Changes for Repositories

* `build.gradle` 
* `settings.gradle`
* `buildSrc/build.gradle` 两处
* `buildSrc/settings.gradle`

```text
maven {
    url "https://mirrors.huaweicloud.com/repository/maven/"
}
maven {
    url 'https://maven.aliyun.com/repository/public/'
}
maven {
    url 'https://maven.aliyun.com/repository/central'
}
maven {
    url 'https://maven.aliyun.com/repository/google'
}
maven {
    url "https://maven.aliyun.com/repository/gradle-plugin"
}
maven {
    url "https://maven.aliyun.com/repository/spring-plugin"
}
maven {
    url "https://maven.aliyun.com/repository/spring"
}
maven {
    url "https://maven.aliyun.com/repository/spring-plugin"
}
maven {
    url "https://repo.spring.io/snapshot"
}
maven {
    url "https://repo.spring.io/milestone"
}
```