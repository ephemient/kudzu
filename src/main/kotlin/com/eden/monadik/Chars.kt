package com.eden.monadik

class CharNode(private val char: Char, name: String, context: NodeContext) : Node(name, context) {
    override fun printAst(currentIndent: Int): String {
        return "${indent(currentIndent)}(CharNode$nodeName: '$char')"
    }
}

class CharParser(name: String = "") : Parser(name) {
    override fun parse(input: ParserContext): Pair<Node, ParserContext> {
        if (input.isEmpty()) throw ParserException("nothing to parse", this, input)
        return Pair(CharNode(input.next(), name, NodeContext(input, input.remaining())), input.remaining())
    }
}

class CharInParser(private vararg val chars: Char, name: String = "") : Parser(name) {
    override fun parse(input: ParserContext): Pair<Node, ParserContext> {
        if (input.isEmpty()) throw ParserException("nothing to parse", this, input)
        if (!chars.contains(input.next())) throw ParserException("char '${input.next()}' not in [${chars.joinToString()}]", this, input)

        return Pair(CharNode(input.next(), name, NodeContext(input, input.remaining())), input.remaining())
    }
}

class CharNotInParser(private vararg val chars: Char, name: String = "") : Parser(name) {
    override fun parse(input: ParserContext): Pair<Node, ParserContext> {
        if (input.isEmpty()) throw ParserException("nothing to parse", this, input)
        if (chars.contains(input.next())) throw ParserException("char '${input.next()}' cannot be in [${chars.joinToString()}]", this, input)

        return Pair(CharNode(input.next(), name, NodeContext(input, input.remaining())), input.remaining())
    }
}

class WhitespaceParser(name: String = "") : Parser(name) {
    override fun parse(input: ParserContext): Pair<Node, ParserContext> {
        if (input.isEmpty()) throw ParserException("nothing to parse", this, input)
        if (!input.next().isWhitespace()) throw ParserException("char '${input.next()}' must be whitespace", this, input)

        return Pair(CharNode(input.next(), name, NodeContext(input, input.remaining())), input.remaining())
    }
}

class DigitParser(name: String = "") : Parser(name) {
    override fun parse(input: ParserContext): Pair<Node, ParserContext> {
        if (input.isEmpty()) throw ParserException("nothing to parse", this, input)
        if (!input.next().isDigit()) throw ParserException("char '${input.next()}' must be a digit", this, input)

        return Pair(CharNode(input.next(), name, NodeContext(input, input.remaining())), input.remaining())
    }
}

class LetterParser(name: String = "") : Parser(name) {
    override fun parse(input: ParserContext): Pair<Node, ParserContext> {
        if (input.isEmpty()) throw ParserException("nothing to parse", this, input)
        if (!input.next().isLetter()) throw ParserException("char '${input.next()}' must be a letter", this, input)

        return Pair(CharNode(input.next(), name, NodeContext(input, input.remaining())), input.remaining())
    }
}