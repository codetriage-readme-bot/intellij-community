package com.intellij.codeInsight.daemon.impl.quickfix;

import com.intellij.codeInsight.CodeInsightUtilBase;
import com.intellij.codeInsight.daemon.QuickFixBundle;
import com.intellij.codeInsight.intention.IntentionAction;
import com.intellij.openapi.command.undo.UndoUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.editor.Editor;
import com.intellij.openapi.project.Project;
import com.intellij.psi.*;
import com.intellij.psi.util.PsiMatcherImpl;
import com.intellij.psi.util.PsiMatchers;
import com.intellij.util.IncorrectOperationException;
import org.jetbrains.annotations.NotNull;

public class InsertSuperFix implements IntentionAction {
  private static final Logger LOG = Logger.getInstance("#com.intellij.codeInsight.daemon.impl.quickfix.InsertSuperFix");

  private final PsiMethod myConstructor;

  public InsertSuperFix(PsiMethod constructor) {
    myConstructor = constructor;
  }

  @NotNull
  public String getText() {
    return QuickFixBundle.message("insert.super.constructor.call.text");
  }

  @NotNull
  public String getFamilyName() {
    return QuickFixBundle.message("insert.super.constructor.call.family");
  }

  public boolean isAvailable(@NotNull Project project, Editor editor, PsiFile file) {
    return myConstructor.isValid()
        && myConstructor.getBody() != null
        && myConstructor.getBody().getLBrace() != null
        && myConstructor.getManager().isInProject(myConstructor)
    ;
  }

  public void invoke(@NotNull Project project, Editor editor, PsiFile file) {
    if (!CodeInsightUtilBase.prepareFileForWrite(myConstructor.getContainingFile())) return;
    try {
      PsiStatement superCall =
        JavaPsiFacade.getInstance(myConstructor.getProject()).getElementFactory().createStatementFromText("super();",null);

      PsiCodeBlock body = myConstructor.getBody();
      PsiJavaToken lBrace = body.getLBrace();
      body.addAfter(superCall, lBrace);
      lBrace = (PsiJavaToken) new PsiMatcherImpl(body)
                .firstChild(PsiMatchers.hasClass(PsiExpressionStatement.class))
                .firstChild(PsiMatchers.hasClass(PsiMethodCallExpression.class))
                .firstChild(PsiMatchers.hasClass(PsiExpressionList.class))
                .firstChild(PsiMatchers.hasClass(PsiJavaToken.class))
                .dot(PsiMatchers.hasText("("))
                .getElement();
      editor.getCaretModel().moveToOffset(lBrace.getTextOffset()+1);
      UndoUtil.markPsiFileForUndo(file);
    }
    catch (IncorrectOperationException e) {
      LOG.error(e);
    }
  }

  public boolean startInWriteAction() {
    return true;
  }

}
