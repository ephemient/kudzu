package com.copperleaf.kudzu.parser

import com.copperleaf.kudzu.node.Node

@ExperimentalStdlibApi
abstract class Parser<NodeType : Node> {

    abstract fun predict(input: ParserContext): Boolean

    abstract val parse: DeepRecursiveFunction<ParserContext, ParserResult<NodeType>>
}