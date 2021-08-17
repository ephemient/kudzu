package com.copperleaf.kudzu.node.choice

import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.NodeContext
import com.copperleaf.kudzu.node.NonTerminalNode

sealed class Choice8Node<T1 : Node, T2 : Node, T3 : Node, T4: Node, T5: Node, T6: Node, T7: Node, T8: Node>(
    val node: Node,
    context: NodeContext
) : NonTerminalNode(context) {
    override val children: List<Node> = listOf(node)
    override val astNodeName: String get() = "Choice8Node.${this::class.simpleName!!}"

    class Option1<T1 : Node, T2 : Node, T3 : Node, T4: Node, T5: Node, T6: Node, T7: Node, T8: Node>(
        node: T1,
        context: NodeContext
    ) : Choice8Node<T1, T2, T3, T4, T5, T6, T7, T8>(node, context)
    class Option2<T1 : Node, T2 : Node, T3 : Node, T4: Node, T5: Node, T6: Node, T7: Node, T8: Node>(
        node: T2,
        context: NodeContext
    ) : Choice8Node<T1, T2, T3, T4, T5, T6, T7, T8>(node, context)
    class Option3<T1 : Node, T2 : Node, T3 : Node, T4: Node, T5: Node, T6: Node, T7: Node, T8: Node>(
        node: T3,
        context: NodeContext
    ) : Choice8Node<T1, T2, T3, T4, T5, T6, T7, T8>(node, context)
    class Option4<T1 : Node, T2 : Node, T3 : Node, T4: Node, T5: Node, T6: Node, T7: Node, T8: Node>(
        node: T4,
        context: NodeContext
    ) : Choice8Node<T1, T2, T3, T4, T5, T6, T7, T8>(node, context)
    class Option5<T1 : Node, T2 : Node, T3 : Node, T4: Node, T5: Node, T6: Node, T7: Node, T8: Node>(
        node: T5,
        context: NodeContext
    ) : Choice8Node<T1, T2, T3, T4, T5, T6, T7, T8>(node, context)
    class Option6<T1 : Node, T2 : Node, T3 : Node, T4: Node, T5: Node, T6: Node, T7: Node, T8: Node>(
        node: T6,
        context: NodeContext
    ) : Choice8Node<T1, T2, T3, T4, T5, T6, T7, T8>(node, context)
    class Option7<T1 : Node, T2 : Node, T3 : Node, T4: Node, T5: Node, T6: Node, T7: Node, T8: Node>(
        node: T7,
        context: NodeContext
    ) : Choice8Node<T1, T2, T3, T4, T5, T6, T7, T8>(node, context)
    class Option8<T1 : Node, T2 : Node, T3 : Node, T4: Node, T5: Node, T6: Node, T7: Node, T8: Node>(
        node: T8,
        context: NodeContext
    ) : Choice8Node<T1, T2, T3, T4, T5, T6, T7, T8>(node, context)
}