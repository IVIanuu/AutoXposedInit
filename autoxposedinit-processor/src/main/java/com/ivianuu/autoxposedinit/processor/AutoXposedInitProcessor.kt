package com.ivianuu.autoxposedinit.processor

import com.google.auto.service.AutoService
import com.ivianuu.autoxposedinit.AutoXposedInit
import java.io.BufferedWriter
import java.io.OutputStreamWriter
import java.nio.charset.StandardCharsets.UTF_8
import javax.annotation.processing.AbstractProcessor
import javax.annotation.processing.Processor
import javax.annotation.processing.RoundEnvironment
import javax.lang.model.SourceVersion
import javax.lang.model.element.TypeElement
import javax.tools.Diagnostic
import javax.tools.StandardLocation
import javax.lang.model.type.TypeMirror
import javax.lang.model.type.DeclaredType
import javax.lang.model.type.TypeKind

/**
 * Generates the xposed_init file
 */
@AutoService(Processor::class)
class AutoXposedInitProcessor : AbstractProcessor() {

    private val types = mutableSetOf<String>()

    override fun getSupportedSourceVersion(): SourceVersion = SourceVersion.latest()

    override fun getSupportedAnnotationTypes() = mutableSetOf(
        AutoXposedInit::class.java.name
    )

    override fun process(set: MutableSet<out TypeElement>, roundEnv: RoundEnvironment): Boolean {
        if (roundEnv.processingOver()) {
            generateFile()
        } else {
            collectItems(roundEnv)
        }

        return false
    }

    private fun collectItems(roundEnv: RoundEnvironment) {
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "collect items")

        roundEnv.getElementsAnnotatedWith(AutoXposedInit::class.java)
            .filter { element ->
                isSupportedType(element.asType()).also {
                    if (!it) {
                        processingEnv.messager.printMessage(Diagnostic.Kind.ERROR,
                            "element is a unsupported type", element) }
                }
            }
            .map { it.asType().toString() }
            .forEach { types.add(it) }
    }

    private fun generateFile() {
        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "generate file")

        val fileObject = processingEnv.filer.createResource(
            StandardLocation.CLASS_OUTPUT, "", "assets/xposed_init"
        )

        val out = fileObject.openOutputStream()

        val writer = BufferedWriter(OutputStreamWriter(out, UTF_8))
        types.forEach {
            writer.write(it)
            writer.newLine()
        }
        writer.flush()
        out.close()

        processingEnv.messager.printMessage(Diagnostic.Kind.NOTE, "wrote file types $types")
    }

    private fun isSupportedType(typeMirror: TypeMirror): Boolean {
        return SUPPORTED_TYPES.any { isSubtypeOfType(typeMirror, it) }
    }

    private fun isSubtypeOfType(typeMirror: TypeMirror, otherType: String): Boolean {
        if (isTypeEqual(typeMirror, otherType)) {
            return true
        }
        if (typeMirror.kind != TypeKind.DECLARED) {
            return false
        }
        val declaredType = typeMirror as DeclaredType
        val typeArguments = declaredType.typeArguments
        if (typeArguments.size > 0) {
            val typeString = StringBuilder(declaredType.asElement().toString())
            typeString.append('<')
            for (i in typeArguments.indices) {
                if (i > 0) {
                    typeString.append(',')
                }
                typeString.append('?')
            }
            typeString.append('>')
            if (typeString.toString() == otherType) {
                return true
            }
        }
        val element = declaredType.asElement() as? TypeElement ?: return false
        val superType = element.superclass
        if (isSubtypeOfType(superType, otherType)) {
            return true
        }
        for (interfaceType in element.interfaces) {
            if (isSubtypeOfType(interfaceType, otherType)) {
                return true
            }
        }
        return false
    }

    private fun isTypeEqual(typeMirror: TypeMirror, otherType: String): Boolean {
        return otherType == typeMirror.toString()
    }

    private companion object {
        private val SUPPORTED_TYPES = setOf(
            "de.robv.android.xposed.IXposedHookInitPackageResources",
            "de.robv.android.xposed.IXposedHookLoadPackage",
            "de.robv.android.xposed.IXposedHookZygoteInit"
        )
    }
}