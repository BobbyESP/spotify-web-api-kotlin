@file:Suppress("UnstableApiUsage")

import com.fasterxml.jackson.databind.json.JsonMapper
import org.jetbrains.dokka.gradle.DokkaTask
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.plugin.KotlinJsCompilerType
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.targets.js.webpack.KotlinWebpackOutput.Target

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    `maven-publish`
    signing
    alias(libs.plugins.android.library)
    alias(libs.plugins.kotlin.serialization)
    alias(libs.plugins.spotless)
    alias(libs.plugins.node)
    alias(libs.plugins.dokka)
}

repositories {
    google()
    mavenCentral()
}

// --- spotify-web-api-kotlin info ---
val libraryVersion = System.getenv("SPOTIFY_API_PUBLISH_VERSION") ?: "0.0.0.SNAPSHOT"

// Publishing credentials (environment variable)
val nexusUsername: String? = System.getenv("NEXUS_USERNAME")
val nexusPassword: String? = System.getenv("NEXUS_PASSWORD")

group = "com.adamratzman"
version = libraryVersion

android {
    namespace = "com.adamratzman.spotify"
    compileSdk = 35

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_21
        targetCompatibility = JavaVersion.VERSION_21
    }

    packaging {
        resources.excludes.add("META-INF/*.md") // needed to prevent android compilation errors
    }

    defaultConfig {
        minSdk = 23
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }

    testOptions {
        unitTests.isReturnDefaultValues = true
    }

    sourceSets {
        getByName("main").setRoot("src/androidMain")
        getByName("test").setRoot("src/androidUnitTest")
    }
}

// invoked in kotlin closure, needs to be registered before
val dokkaJar by tasks.registering(Jar::class) {
    group = JavaBasePlugin.DOCUMENTATION_GROUP
    description = "spotify-web-api-kotlin generated documentation"
    from(tasks.dokkaHtml)
    archiveClassifier.set("javadoc")
}

kotlin {
    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.add("-Xexpect-actual-classes")
    }

    explicitApiWarning()
    jvmToolchain(21)

    androidTarget {
        compilations.all {
            kotlinOptions.jvmTarget = "21"
        }

        mavenPublication { setupPom(artifactId) }

        publishLibraryVariants("debug", "release")
        publishLibraryVariantsGroupedByFlavor = true
    }

    jvm {
        compilations.all {
            kotlinOptions.jvmTarget = "21"
        }
        testRuns["test"].executionTask.configure {
            useJUnit()
        }

        mavenPublication { setupPom(artifactId) }
    }

    js(KotlinJsCompilerType.IR) {
        mavenPublication { setupPom(artifactId) }

        browser {
            webpackTask {
                output.globalObject = "this"
                output.libraryTarget = Target.UMD
            }

            testTask {
                useKarma {
                    useChromeHeadless()
                    webpackConfig.cssSupport { isEnabled = true }
                }
            }
        }

        binaries.executable()
    }

    macosX64 {
        mavenPublication { setupPom(artifactId) }
    }

    linuxX64 {
        mavenPublication { setupPom(artifactId) }
    }

    mingwX64 {
        mavenPublication { setupPom(artifactId) }
    }

    iosX64 {
        binaries { framework { baseName = "spotify" } }
        mavenPublication { setupPom(artifactId) }
    }

    iosArm64 {
        binaries { framework { baseName = "spotify" } }
        mavenPublication { setupPom(artifactId) }
    }

    iosSimulatorArm64 {
        binaries { framework { baseName = "spotify" } }
        mavenPublication { setupPom(artifactId) }
    }

    // Apply default hierarchy template for source sets
    applyDefaultHierarchyTemplate()

    sourceSets {
        commonMain {
            dependencies {
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.ktor.client.core)
                implementation(libs.korlibs.krypto)
                implementation(libs.korlibs.korim)
                implementation(libs.kotlinx.datetime)
                implementation(libs.kotlinx.coroutines.core)
            }
        }

        commonTest {
            dependencies {
                implementation(libs.kotlinx.coroutines.test)
                implementation(kotlin("test-common"))
                implementation(kotlin("test-annotations-common"))
            }
        }

        val commonJvmLikeMain by creating {
            dependsOn(commonMain.get())

            dependencies {
                implementation(libs.android.retrofuture)
            }
        }

        val commonJvmLikeTest by creating {
            dependsOn(commonTest.get())

            dependencies {
                implementation(kotlin("test-junit"))
                implementation(libs.spark.core)
                runtimeOnly(kotlin("reflect"))
            }
        }

        val commonNonJvmTargetsTest by creating {
            dependsOn(commonTest.get())
        }

        jvmMain {
            dependsOn(commonJvmLikeMain)

            dependencies {
                implementation(libs.ktor.client.cio)
            }
        }

        jvmTest.get().dependsOn(commonJvmLikeTest)

        jsMain {
            dependencies {
                implementation(libs.ktor.client.js)
                implementation(kotlin("stdlib-js"))
            }
        }

        jsTest {
            dependsOn(commonNonJvmTargetsTest)

            dependencies {
                implementation(kotlin("test-js"))
            }
        }

        androidMain {
            dependsOn(commonJvmLikeMain)

            dependencies {
                api(libs.android.spotify.auth)
                implementation(libs.ktor.client.okhttp)
                implementation(libs.android.crypto)
                implementation(libs.androidx.appcompat)
            }
        }

        val androidUnitTest by getting {
            dependsOn(commonJvmLikeTest)
        }

        // desktop targets
        val desktopMain by creating {
            dependsOn(commonMain.get())

            dependencies {
                implementation(libs.ktor.client.curl)
            }
        }

        linuxMain.get().dependsOn(desktopMain)
        mingwMain.get().dependsOn(desktopMain)
        macosMain.get().dependsOn(desktopMain)

        val desktopTest by creating {
            dependsOn(commonNonJvmTargetsTest)
        }

        linuxTest.get().dependsOn(desktopTest)
        mingwTest.get().dependsOn(desktopTest)
        macosTest.get().dependsOn(desktopTest)

        // darwin targets
        val nativeDarwinMain by creating {
            dependsOn(commonMain.get())

            dependencies {
                implementation(libs.ktor.client.ios)
            }
        }

        val nativeDarwinTest by creating {
            dependsOn(commonNonJvmTargetsTest)
        }

        iosMain.get().dependsOn(nativeDarwinMain)
        iosTest.get().dependsOn(nativeDarwinTest)

        all {
            languageSettings.optIn("kotlin.RequiresOptIn")
            languageSettings.optIn("kotlinx.serialization.ExperimentalSerializationApi")
        }
    }

    publishing {
        registerPublishing()
    }
}

tasks {
    dokkaHtml {
        outputDirectory.set(projectDir.resolve("docs"))

        dokkaSourceSets {
            configureEach {
                skipDeprecated.set(true)

                sourceLink {
                    localDirectory.set(file("src"))
                    remoteUrl.set(uri("https://github.com/adamint/spotify-web-api-kotlin/tree/master/src").toURL())
                    remoteLineSuffix.set("#L")
                }
            }
        }
    }

    spotless {
        kotlin {
            target("**/*.kt")
            licenseHeader("/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2023; Original author: Adam Ratzman */")
            ktlint()
        }
    }

    register<Task>("publishAllPublicationsToNexusRepositoryWithTests") {
        dependsOn(check)
        dependsOn("publishAllPublicationsToNexusRepository")
        dependsOn(dokkaHtml)
    }

    withType<Test> {
        testLogging {
            showStandardStreams = true
        }
    }

    register<Sync>("packForXcode") {
        group = "build"
        val mode = System.getenv("CONFIGURATION") ?: "DEBUG"
        val sdkName = System.getenv("SDK_NAME") ?: "iphonesimulator"
        val targetName = "ios" + if (sdkName.startsWith("iphoneos")) "Arm64" else "X64"
        val framework = kotlin.targets.getByName<KotlinNativeTarget>(targetName).binaries.getFramework(mode)
        inputs.property("mode", mode)
        dependsOn(framework.linkTaskProvider)
        val targetDir = File(layout.buildDirectory.asFile.get(), "xcode-frameworks")
        from({ framework.outputDirectory })
        into(targetDir)
    }

    named("build") {
        dependsOn("packForXcode")
    }
}

// Configure signing tasks to run before publishing
val signingTasks = tasks.withType<Sign>()
tasks.withType<AbstractPublishToMaven>().configureEach {
    dependsOn(signingTasks)
}

fun MavenPublication.setupPom(publicationName: String) {
    artifactId = artifactId.replace("-web", "")
    artifact(dokkaJar.get()) // add javadocs to publication

    pom {
        name.set(publicationName)
        description.set("A Kotlin wrapper for the Spotify Web API.")
        url.set("https://github.com/adamint/spotify-web-api-kotlin")
        inceptionYear.set("2018")

        scm {
            url.set("https://github.com/adamint/spotify-web-api-kotlin")
            connection.set("scm:https://github.com/adamint/spotify-web-api-kotlin.git")
            developerConnection.set("scm:git://github.com/adamint/spotify-web-api-kotlin.git")
        }

        licenses {
            license {
                name.set("MIT License")
                url.set("https://github.com/adamint/spotify-web-api-kotlin/blob/master/LICENSE")
                distribution.set("repo")
            }
        }

        developers {
            developer {
                id.set("adamratzman")
                name.set("Adam Ratzman")
                email.set("adam@adamratzman.com")
            }
        }
    }
}

// --- Publishing ---
fun PublishingExtension.registerPublishing() {
    publications {
        val kotlinMultiplatform by getting(MavenPublication::class) {
            artifactId = "spotify-api-kotlin-core"
            setupPom(artifactId)
        }
    }

    repositories {
        maven {
            name = "nexus"

            // Publishing locations
            val releasesRepoUrl = "https://oss.sonatype.org/service/local/staging/deploy/maven2/"
            val snapshotsRepoUrl = "https://oss.sonatype.org/content/repositories/snapshots/"

            url = uri(if (version.toString().endsWith("SNAPSHOT")) snapshotsRepoUrl else releasesRepoUrl)

            credentials {
                username = nexusUsername
                password = nexusPassword
            }
        }
    }
}

// --- Signing ---
val signingKey = project.findProperty("SIGNING_KEY") as? String
val signingPassword = project.findProperty("SIGNING_PASSWORD") as? String

signing {
    if (signingKey != null && signingPassword != null) {
        useInMemoryPgpKeys(signingKey, signingPassword)
        sign(publishing.publications)
    }
}

// Test tasks
tasks.register("updateNonJvmTestFakes") {
    if (System.getenv("SPOTIFY_TOKEN_STRING") == null
        || System.getenv("SHOULD_RECACHE_RESPONSES")?.toBoolean() != true
    ) {
        return@register
    }

    dependsOn("jvmTest")
    val responseCacheDir =
        System.getenv("RESPONSE_CACHE_DIR")?.let { File(it) }
            ?: throw IllegalArgumentException("No response cache directory provided")
    val commonTestResourcesSource = projectDir.resolve("src/commonTest/resources")
    if (!commonTestResourcesSource.exists()) commonTestResourcesSource.mkdir()

    val commonTestResourceFileToSet = commonTestResourcesSource.resolve("cached_responses.json")

    if (commonTestResourceFileToSet.exists()) commonTestResourceFileToSet.delete()
    commonTestResourceFileToSet.createNewFile()

    val testToOrderedResponseMap: Map<String, List<String>> = responseCacheDir.walk()
        .filter { it.isFile && it.name.matches("http_request_\\d+.txt".toRegex()) }
        .groupBy { "${it.parentFile.parentFile.name}.${it.parentFile.name}" }
        .map { (key, group) -> key to group.sorted().map { it.readText() } }
        .toMap()

    val jsonLiteral = JsonMapper().writeValueAsString(testToOrderedResponseMap)
    commonTestResourceFileToSet.writeText(jsonLiteral)
    println(commonTestResourceFileToSet.absolutePath)
}