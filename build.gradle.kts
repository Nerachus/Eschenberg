import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
	kotlin("jvm") version Versions.kotlin
}

allprojects {
	version = App.versionName

	repositories {
		jcenter()
	}
}

subprojects {
	apply(plugin = "kotlin")
	configure<JavaPluginConvention> {
		sourceCompatibility = Versions.java
		targetCompatibility = Versions.java
	}
	tasks.withType<JavaCompile> {
		options.isIncremental = true
		options.encoding = "UTF-8"
	}
	tasks.withType<KotlinCompile> {
		kotlinOptions.jvmTarget = Versions.jvm
	}
}
