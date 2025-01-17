package com.copperleaf.kudzu.node.chars

import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.TerminalNode
import com.copperleaf.kudzu.parser.chars.NewlineCharParser

/**
 * A Node parsed from a [NewlineCharParser], indicating a platform-independent newline.
 */
class EndOfInputNode(
    context: NodeContext
) : TerminalNode(context) {
    override val text: String = "EOF"
}
