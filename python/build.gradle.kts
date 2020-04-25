import org.apache.tools.ant.taskdefs.condition.Os

plugins {
    java
    idea
    id("com.jetbrains.python.envs") version "0.0.30"
}

envs {
    bootstrapDirectory = File(buildDir, "bootstrap")
    envsDirectory = file(buildDir)

    python("python-3.8.2", "3.8.2")
    virtualenv("virtualenv-3.8.2", "python-3.8.2")
}

tasks {
    build {
        dependsOn("build_envs")
    }
    register<Exec>("pythonRun") {
        group = "python"
        executable = envs.virtualEnvs.first().pythonPath()
        workingDir = file("main")
        args("numpy_test.py")
        dependsOn("pipInstall")
    }

    register<Exec>("pipInstall") {
        group = "python"
        executable = envs.virtualEnvs.first().pipPath()
        args("install", "-r", "requirements.txt")

        dependsOn("build_envs")

    }

}

fun com.jetbrains.python.envs.VirtualEnv.pythonPath(): String {
    val path = envDir.absolutePath
    return if (Os.isFamily(Os.FAMILY_WINDOWS))
        "$path\\Scripts\\python.exe"
    else
        "$path\\Scripts\\python"
}

fun com.jetbrains.python.envs.VirtualEnv.pipPath(): String {
    val path = envDir.absolutePath
    return if (Os.isFamily(Os.FAMILY_WINDOWS))
        "$path\\Scripts\\pip.exe"
    else
        "$path\\Scripts\\pip"
}