package com.jetbrains.python;

import com.intellij.lang.CodeDocumentationAwareCommenter;
import com.intellij.psi.PsiComment;
import com.intellij.psi.tree.IElementType;

/**
 * @author yole
 */
public class PythonCommenter implements CodeDocumentationAwareCommenter {
  public String getLineCommentPrefix() {
    return "#";
  }

  public String getBlockCommentPrefix() {
    return null;
  }

  public String getBlockCommentSuffix() {
    return null;
  }

  public String getCommentedBlockCommentPrefix() {
    return null;
  }

  public String getCommentedBlockCommentSuffix() {
    return null;
  }

  @Override
  public IElementType getLineCommentTokenType() {
    return PyTokenTypes.END_OF_LINE_COMMENT;
  }

  @Override
  public IElementType getBlockCommentTokenType() {
    return null;
  }

  @Override
  public IElementType getDocumentationCommentTokenType() {
    return null;
  }

  @Override
  public String getDocumentationCommentPrefix() {
    return null;
  }

  @Override
  public String getDocumentationCommentLinePrefix() {
    return null;
  }

  @Override
  public String getDocumentationCommentSuffix() {
    return null;
  }

  @Override
  public boolean isDocumentationComment(PsiComment element) {
    return false;
  }
}
