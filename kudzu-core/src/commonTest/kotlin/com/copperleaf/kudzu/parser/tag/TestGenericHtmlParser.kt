package com.copperleaf.kudzu.parser.tag

import com.copperleaf.kudzu.expectThat
import com.copperleaf.kudzu.isEqualTo
import com.copperleaf.kudzu.isNotNull
import com.copperleaf.kudzu.isTrue
import com.copperleaf.kudzu.node
import com.copperleaf.kudzu.node.Node
import com.copperleaf.kudzu.node.many.ManyNode
import com.copperleaf.kudzu.node.mapped.ValueNode
import com.copperleaf.kudzu.node.sequence.SequenceNode
import com.copperleaf.kudzu.node.tag.TagNode
import com.copperleaf.kudzu.node.text.TextNode
import com.copperleaf.kudzu.parsedCorrectly
import com.copperleaf.kudzu.parser.Parser
import com.copperleaf.kudzu.parser.ParserContext
import com.copperleaf.kudzu.parser.chars.CharInParser
import com.copperleaf.kudzu.parser.many.SeparatedByParser
import com.copperleaf.kudzu.parser.mapped.MappedParser
import com.copperleaf.kudzu.parser.predict.PredictionParser
import com.copperleaf.kudzu.parser.sequence.SequenceParser
import com.copperleaf.kudzu.parser.text.AnyTokenParser
import com.copperleaf.kudzu.parser.text.LiteralTokenParser
import com.copperleaf.kudzu.parser.text.RequiredWhitespaceParser
import com.copperleaf.kudzu.parser.value.AnyLiteralParser
import com.copperleaf.kudzu.test
import kotlin.test.Test

@Suppress("UNCHECKED_CAST")
@ExperimentalStdlibApi
class TestGenericHtmlParser {

    val htmlAttr: Parser<ValueNode<Pair<String, Any>>> = MappedParser(
        SequenceParser(
            AnyTokenParser(),
            CharInParser('='),
            AnyLiteralParser()
        )
    ) {
        val (key, _, value) = it.children

        key.text to (value as ValueNode<Any>).value
    }

    @Test
    fun testHtmlAttr() {
        "one=\"two\"".run {
            expectThat(htmlAttr.predict(ParserContext.fromString(this))).isTrue()
            expectThat(htmlAttr.test(this))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.isEqualTo("one" to "two")
                }
        }
        "three=4".run {
            expectThat(htmlAttr.predict(ParserContext.fromString(this))).isTrue()
            expectThat(htmlAttr.test(this))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.isEqualTo("three" to 4)
                }
        }
        "five=6.7".run {
            expectThat(htmlAttr.predict(ParserContext.fromString(this))).isTrue()
            expectThat(htmlAttr.test(this))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.isEqualTo("five" to 6.7)
                }
        }
        "eight=true".run {
            expectThat(htmlAttr.predict(ParserContext.fromString(this))).isTrue()
            expectThat(htmlAttr.test(this))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.isEqualTo("eight" to true)
                }
        }
    }

    val htmlAttrList: Parser<ValueNode<Map<String, Any>>> = MappedParser(
        SeparatedByParser(
            htmlAttr,
            RequiredWhitespaceParser()
        )
    ) {
        it.nodeList.map { it.value }.toMap()
    }

    @Test
    fun testHtmlAttrList() {
        "one=\"two\" three=4".run {
            expectThat(htmlAttrList.predict(ParserContext.fromString(this))).isTrue()
            expectThat(htmlAttrList.test(this))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.isEqualTo(
                        mapOf(
                            "one" to "two",
                            "three" to 4
                        )
                    )
                }
        }

        "five=6.7 eight=true".run {
            expectThat(htmlAttrList.predict(ParserContext.fromString(this))).isTrue()
            expectThat(htmlAttrList.test(this))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.isEqualTo(
                        mapOf(
                            "five" to 6.7,
                            "eight" to true
                        )
                    )
                }
        }
        "one=\"two\" three=4 five=6.7 eight=true".run {
            expectThat(htmlAttrList.predict(ParserContext.fromString(this))).isTrue()
            expectThat(htmlAttrList.test(this))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.isEqualTo(
                        mapOf(
                            "one" to "two",
                            "three" to 4,
                            "five" to 6.7,
                            "eight" to true
                        )
                    )
                }
        }
    }

    val openTag: Parser<ValueNode<Pair<String, Map<String, Any>>>> = MappedParser(
        SequenceParser(
            PredictionParser(
                SequenceParser(
                    CharInParser('<'),
                    AnyTokenParser(),
                )
            ),
            RequiredWhitespaceParser(),
            htmlAttrList,
            CharInParser('>'),
        )
    ) {
        val (tagNameSequence, _, attrMap) = it.children
        val (_, tagName) = (tagNameSequence as SequenceNode).children

        tagName.text to (attrMap as ValueNode<Map<String, Any>>).value
    }

    @Test
    fun testHtmlTagOpen() {
        "<a one=\"two\" three=4 five=6.7 eight=true>".run {
            expectThat(openTag.predict(ParserContext.fromString(this))).isTrue()
            expectThat(openTag.test(this))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.first.isEqualTo("a")
                    value.second.isEqualTo(
                        mapOf(
                            "one" to "two",
                            "three" to 4,
                            "five" to 6.7,
                            "eight" to true
                        )
                    )
                }
        }
        "<a one=\"two\" three=4 five=6.7 eight=true>".run {
            expectThat(openTag.predict(ParserContext.fromString(this))).isTrue()
            expectThat(openTag.test(this, logErrors = true))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.first.isEqualTo("a")
                    value.second.isEqualTo(
                        mapOf(
                            "one" to "two",
                            "three" to 4,
                            "five" to 6.7,
                            "eight" to true
                        )
                    )
                }
        }
    }

    val closeTag: Parser<ValueNode<String>> = MappedParser(
        SequenceParser(
            LiteralTokenParser("</"),
            AnyTokenParser(),
            CharInParser('>'),
        )
    ) {
        val (_, tagName, _) = it.children

        tagName.text
    }

    @Test
    fun testHtmlTagClose() {
        "</a>".run {
            expectThat(closeTag.predict(ParserContext.fromString(this))).isTrue()
            expectThat(closeTag.test(this))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.isEqualTo("a")
                }
        }
        "</blockquote>".run {
            expectThat(closeTag.predict(ParserContext.fromString(this))).isTrue()
            expectThat(closeTag.test(this, logErrors = true))
                .parsedCorrectly()
                .node()
                .isNotNull()
                .apply {
                    value.isEqualTo("blockquote")
                }
        }
    }

    val tagParser = TagParser(
        listOf(
            TagBuilder(
                "html",
                openTag,
                closeTag
            )
        ),
        allowSameTagRecursion = true
    )

    @Test
    fun testHtmlTagParser() {
        (
            "before tag " +
                "<a one=\"two\" three=4 five=6.7 eight=true>" +
                "hello world" +
                "</a> " +
                "after tag"
            ).run {
            expectThat(tagParser.predict(ParserContext.fromString(this))).isTrue()
            expectThat(tagParser.test(this, logErrors = true))
                .parsedCorrectly()
        }
        (
            "before tag " +
                "<a one=\"two\" three=4 five=6.7 eight=true>" +
                "goodbye" +
                " <b one=2>cruel</b>" +
                " world" +
                "</a> " +
                "after tag"
            ).run {
            expectThat(tagParser.predict(ParserContext.fromString(this))).isTrue()
            expectThat(tagParser.test(this))
                .parsedCorrectly(
                    """
                    |(ManyNode:
                    |  (TextNode: 'before tag ')
                    |  (TagNode:
                    |    (ValueNode: '(a, {one=two, three=4, five=6.7, eight=true})')
                    |    (ManyNode:
                    |      (TextNode: 'goodbye ')
                    |      (TagNode:
                    |        (ValueNode: '(b, {one=2})')
                    |        (ManyNode:
                    |          (TextNode: 'cruel')
                    |        )
                    |      )
                    |      (TextNode: ' world')
                    |    )
                    |  )
                    |  (TextNode: ' after tag')
                    |)
                """.trimMargin()
                )
                .node()
                .isNotNull()
                .apply {
                    (this.nodeList[0] as? TextNode)
                        .isNotNull()
                        .text
                        .isEqualTo("before tag ")

                    (this.nodeList[1] as? TagNode<*, *>)
                        .isNotNull()
                        .also { tagNode ->
                            (tagNode.opening as? ValueNode<Pair<String, Map<String, Any>>>)
                                .isNotNull()
                                .value.also { (tagName, attrMap) ->
                                    tagName.isEqualTo("a")
                                    attrMap.isEqualTo(
                                        mapOf(
                                            "one" to "two",
                                            "three" to 4,
                                            "five" to 6.7,
                                            "eight" to true
                                        )
                                    )
                                }
                            (tagNode.content as? ManyNode<Node>)
                                .isNotNull()
                                .nodeList
                                .also { contentNodes ->
                                    (contentNodes[0] as? TextNode)
                                        .isNotNull()
                                        .text
                                        .isEqualTo("goodbye ")
                                    (contentNodes[1] as? TagNode<*, *>)
                                        .isNotNull()
                                        .also { innerTag ->
                                            (innerTag.opening as? ValueNode<Pair<String, Map<String, Any>>>)
                                                .isNotNull()
                                                .value.also { (tagName, attrMap) ->
                                                    tagName.isEqualTo("b")
                                                    attrMap.isEqualTo(mapOf("one" to 2))
                                                }
                                        }
                                    (contentNodes[2] as? TextNode)
                                        .isNotNull()
                                        .text
                                        .isEqualTo(" world")
                                }
                        }

                    (this.nodeList[2] as? TextNode)
                        .isNotNull()
                        .text
                        .isEqualTo(" after tag")
                }
        }
    }
}
