# CommandUtils
A streamlined, customizable command framework for easy and efficient command creation

# How to install it?

**Gradle**

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}
```

```groovy
dependencies {
    implementation 'com.github.vansencool:CommandUtils:1.2.2'
}
```

**Maven**

```xml
<repository>
    <id>jitpack.io</id>
    <url>https://jitpack.io</url>
</repository>
```

```xml
<dependency>
    <groupId>com.github.vansencool</groupId>
    <artifactId>CommandUtils</artifactId>
    <version>1.2.2</version>
</dependency>
```

# How to use it?
First off, you'll need to give CommandAPI an instance of your lifecycle event, either your plugin
```java
package com.example.myplugin;

import dev.vansen.commandutils.api.CommandAPI;
import org.bukkit.plugin.java.JavaPlugin;

public final class MyPlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        CommandAPI.set(this);
        // CommandAPI.set(this.getLifecycleManager());
    }
}
```
After you've done that, you can visit https://vansen.gitbook.io/commandutils/ on how to use it!
