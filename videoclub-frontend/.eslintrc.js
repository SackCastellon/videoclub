module.exports = {
    root: true,
    env: {
        node: true
    },
    extends: ["plugin:vue/recommended", "@vue/typescript"],
    rules: {
        "no-console": process.env.NODE_ENV === "production" ? "error" : "off",
        "no-debugger": process.env.NODE_ENV === "production" ? "error" : "off",
        "vue/html-closing-bracket-newline": ["warn", {"multiline": "never"}],
    },
    parserOptions: {
        parser: "@typescript-eslint/parser"
    }
};
