package com.copperleaf.kudzu.node.expression

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.NonTerminalNode

class BinaryOperationNode(
    val operator: Node,
    val operand: Node,
    context: NodeContext
) : NonTerminalNode(context) {
    override val children: List<Node> = listOf(operator, operand)
}