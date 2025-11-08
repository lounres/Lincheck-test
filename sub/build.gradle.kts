import org.gradle.accessors.dm.LibrariesForVersions
import org.gradle.accessors.dm.RootProjectAccessor
import org.jetbrains.kotlin.gradle.dsl.ExplicitApiMode.Warning

plugins {
    alias(versions.plugins.kotlin.jvm)
}

val Project.versions: LibrariesForVersions get() = rootProject.extensions.getByName<LibrariesForVersions>("versions")
//val Project.libs: LibrariesForLibs get() = rootProject.extensions.getByName<LibrariesForLibs>("libs")
val Project.projects: RootProjectAccessor get() = rootProject.extensions.getByName<RootProjectAccessor>("projects")
fun PluginAware.apply(pluginDependency: PluginDependency) = apply(plugin = pluginDependency.pluginId)
fun PluginAware.apply(pluginDependency: Provider<PluginDependency>) = apply(plugin = pluginDependency.get().pluginId)
fun PluginAware.apply(pluginDependency: ProviderConvertible<PluginDependency>) = apply(plugin = pluginDependency.asProvider().get().pluginId)
fun PluginManager.withPlugin(pluginDep: PluginDependency, block: AppliedPlugin.() -> Unit) = withPlugin(pluginDep.pluginId, block)
fun PluginManager.withPlugin(pluginDepProvider: Provider<PluginDependency>, block: AppliedPlugin.() -> Unit) = withPlugin(pluginDepProvider.get().pluginId, block)
fun PluginManager.withPlugins(vararg pluginDeps: PluginDependency, block: AppliedPlugin.() -> Unit) = pluginDeps.forEach { withPlugin(it, block) }
fun PluginManager.withPlugins(vararg pluginDeps: Provider<PluginDependency>, block: AppliedPlugin.() -> Unit) = pluginDeps.forEach { withPlugin(it, block) }
inline fun <T> Iterable<T>.withEach(action: T.() -> Unit) = forEach { it.action() }

kotlin {
    explicitApi = Warning
    
    compilerOptions {
        freeCompilerArgs.addAll(
            "-Xexpect-actual-classes",
            "-Xconsistent-data-class-copy-visibility",
            "-Xcontext-sensitive-resolution",
            "-Xreturn-value-checker=full",
        )
    }
    
    jvmToolchain {
        languageVersion = JavaLanguageVersion.of(project.extra["jvmTargetVersion"] as String)
        vendor = JvmVendorSpec.matching(project.extra["jvmVendor"] as String)
    }

//    jvm {
//        testRuns.all {
//            executionTask {
//                useJUnitPlatform()
//            }
//        }
//    }
//
//    js {
//        browser()
//        nodejs()
//    }
//
//    @OptIn(ExperimentalWasmDsl::class)
//    wasmJs {
//        browser()
//        nodejs()
//        d8()
//    }
    
    sourceSets {
        all {
            languageSettings {
                progressiveMode = true
                enableLanguageFeature("ContextParameters")
                enableLanguageFeature("ValueClasses")
                enableLanguageFeature("ContractSyntaxV2")
                enableLanguageFeature("ExplicitBackingFields")
                enableLanguageFeature("NestedTypeAliases")
                optIn("kotlin.experimental.ExperimentalTypeInference")
                optIn("kotlin.contracts.ExperimentalContracts")
                optIn("kotlin.ExperimentalStdlibApi")
                optIn("kotlin.ExperimentalSubclassOptIn")
                optIn("kotlin.ExperimentalUnsignedTypes")
                optIn("kotlin.uuid.ExperimentalUuidApi")
                optIn("kotlin.concurrent.atomics.ExperimentalAtomicApi")
                optIn("kotlinx.serialization.ExperimentalSerializationApi")
                optIn("dev.lounres.kone.annotations.UnstableKoneAPI")
                optIn("dev.lounres.kone.annotations.ExperimentalKoneAPI")
            }
        }

//        commonMain {
//            dependencies {
//                implementation(versions.kone.util.misc)
//                api(versions.kotlinx.coroutines.core)
//            }
//        }
//        commonTest {
//            dependencies {
//                implementation(kotlin("test"))
//                implementation(kone.concurrentCollections)
//            }
//        }
//        jvmTest {
//            dependencies {
//                implementation(versions.kotlinx.lincheck)
//            }
//        }
        
        main {
            dependencies {
                implementation(versions.kone.util.misc)
                api(versions.kotlinx.coroutines.core)
            }
        }
        
        test {
            dependencies {
                implementation(kotlin("test"))
                implementation(kone.concurrentCollections)
                implementation(versions.kotlinx.lincheck)
            }
        }
    }
}

tasks.test {
    useJUnitPlatform()
}