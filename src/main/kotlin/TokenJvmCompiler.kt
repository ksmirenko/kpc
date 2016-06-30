package io.github.ksmirenko.kpc

import org.objectweb.asm.ClassWriter
import org.objectweb.asm.Label
import org.objectweb.asm.MethodVisitor
import org.objectweb.asm.Opcodes.*
import java.util.*

class BrainfuckCompiler {
    private val memorySize = 30000
    private val labels = Stack<Label>()
    private var isFirstLoop = true

    fun generateClassByteArray(commands : String, className : String) : ByteArray {
        val cw = ClassWriter(0);
        //val cw = ClassWriter(ClassWriter.COMPUTE_FRAMES or ClassWriter.COMPUTE_MAXS);
        cw.visit(V1_7, ACC_PUBLIC, className, null, "java/lang/Object", null)

        // generating constructor method
        /*val cmv = cw.visitMethod(ACC_PUBLIC, "<init>", "()V", null, null)
        cmv.visitCode()
        val lCStart = Label()
        cmv.visitLabel(lCStart)
        cmv.visitVarInsn(ALOAD, 0)
        cmv.visitMethodInsn(INVOKESPECIAL, "java/lang/Object", "<init>", "()V", false)
        cmv.visitInsn(RETURN)
        val lCEnd = Label()
        cmv.visitLabel(lCEnd)
        cmv.visitLocalVariable("this", "Lhomework/hw09/SampleClass;", null, lCStart, lCEnd, 0)
        cmv.visitMaxs(1, 1)
        cmv.visitEnd()*/

        // generating main method
        val mv = cw.visitMethod(ACC_PUBLIC or ACC_STATIC, "main", "([Ljava/lang/String;)V", null, null)
        mv.visitCode()
        val lStart = Label()
        mv.visitLabel(lStart)
        // creating "memory" array
        mv.visitIntInsn(SIPUSH, memorySize)
        mv.visitIntInsn(NEWARRAY, T_CHAR)
        mv.visitIntInsn(ASTORE, 1)
        // setting memory pointer to zero
        mv.visitInsn(ICONST_0)
        mv.visitIntInsn(ISTORE, 2)

        isFirstLoop = true
        for (op in commands) {
            when (op) {
                Tokens.NEXT.v ->
                    mv.visitPtrModifyInsn(true)
                Tokens.PREV.v ->
                    mv.visitPtrModifyInsn(false)
                Tokens.INC.v ->
                    mv.visitValModifyInsn(true)
                Tokens.DEC.v ->
                    mv.visitValModifyInsn(false)
                Tokens.WRITE.v ->
                    mv.visitWriteInsn()
                Tokens.READ.v ->
                    mv.visitReadInsn()
                Tokens.LBRACKET.v ->
                    mv.visitWhileInsn()
                Tokens.RBRACKET.v ->
                    mv.visitEndwhileInsn()
            }
        }
        // finalizing class
        mv.visitInsn(RETURN)
        val lEnd = Label()
        mv.visitLabel(lEnd)
        /*mv.visitLocalVariable("var0", "[Ljava/lang/String;", null, lStart, lEnd, 0);
        mv.visitLocalVariable("var1", "[C", null, lStart, lEnd, 1);
        mv.visitLocalVariable("var2", "I", null, lStart, lEnd, 2);*/
        mv.visitMaxs(4, 3)
        mv.visitEnd()
        cw.visitEnd()

        return cw.toByteArray()
    }

    /**
     * Creates bytecode for PREV and NEXT token.
     */
    private fun MethodVisitor.visitPtrModifyInsn(isNext : Boolean) {
        if (isNext)
            visitIincInsn(2, 1)
        else
            visitIincInsn(2, memorySize - 1)
        visitVarInsn(ILOAD, 2)
        visitIntInsn(SIPUSH, memorySize)
        visitInsn(IREM)
        visitIntInsn(ISTORE, 2)
    }

    /**
     * Creates bytecode for INC and DEC token.
     */
    private fun MethodVisitor.visitValModifyInsn(isInc : Boolean) {
        visitVarInsn(ALOAD, 1)
        visitVarInsn(ILOAD, 2)
        visitInsn(DUP2)
        visitInsn(CALOAD)
        visitInsn(ICONST_1)
        if (isInc)
            visitInsn(IADD)
        else
            visitInsn(ISUB)
        visitInsn(I2C)
        visitInsn(CASTORE)
    }

    /**
     * Creates bytecode for WRITE token.
     */
    private fun MethodVisitor.visitWriteInsn() {
        visitFieldInsn(GETSTATIC, "java/lang/System", "out", "Ljava/io/PrintStream;")
        visitVarInsn(ALOAD, 1)
        visitVarInsn(ILOAD, 2)
        visitInsn(CALOAD)
        visitMethodInsn(INVOKEVIRTUAL, "java/io/PrintStream", "print", "(C)V", false)
    }

    /**
     * Creates bytecode for READ token.
     */
    private fun MethodVisitor.visitReadInsn() {
        visitVarInsn(ALOAD, 1)
        visitVarInsn(ILOAD, 2)
        visitFieldInsn(GETSTATIC, "java/lang/System", "in", "Ljava/io/InputStream;")
        visitMethodInsn(INVOKEVIRTUAL, "java/io/InputStream", "read", "()I", false)
        visitInsn(CASTORE)
    }

    /**
     * Creates bytecode for LBRACKET token.
     */
    private fun MethodVisitor.visitWhileInsn() {
        val beginLabel = Label()
        val endLabel = Label()
        labels.push(endLabel)
        labels.push(beginLabel)
        visitLabel(beginLabel)
        if (isFirstLoop) {
            visitFrame(F_APPEND, 2, arrayOf("[C", INTEGER), 0, null)
            isFirstLoop = false
        }
        else {
            visitFrame(F_SAME, 0, null, 0, null)
        }

        visitVarInsn(ALOAD, 1)
        visitVarInsn(ILOAD, 2)
        visitInsn(CALOAD)
        visitJumpInsn(IFEQ, endLabel)
    }

    /**
     * Creates bytecode for RBRACKET Brainfuck token.
     */
    private fun MethodVisitor.visitEndwhileInsn() {
        visitJumpInsn(GOTO, labels.pop())
        visitLabel(labels.pop())
        visitFrame(F_SAME, 0, null, 0, null)
    }
}


