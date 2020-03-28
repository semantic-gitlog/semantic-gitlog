# semantic-gitlog {docsify-ignore-all}

一个简单的基于 [Conventional Commits][2] 的 [Semantic Versioning][1] 自动化版本管理工具。
它可以自动推断和管理版本号并生成 [angular-style][3] 风格的更新日志。

本项目的 [CHANGELOG.md][4] 便使用 [这个模板][5] 自动生成。

# 功能模块

* 包
  - **[semantic-commit][11]**: 针对 `Conventional Commits` 的词法分析器
    - 基于 `angular-style`
    - 本地化支持
    - 历史提交本地化映射
  - **[semantic-gitlog][12]**: `angular-style` 风格的更新日志生成器
    - 基于 [Semantic Versioning][1] 的版本推断器
    - 完全基于 [Mustache][10] 模板引擎
    - 本地化支持
    - 多模板多输出支持
* 插件
  - [maven-semantic-gitlog][13]
  - [gradle-semantic-gitlog][14]

# 作者

[@ymind][6]，全栈工程师

# 相关项目

* [semantic-version][7]
* [git-changelog-lib][8] (Apache-2.0)

# 许可证

这是一个（系列）在 [MIT license][9] 许可证之下的开源软件。

[1]: https://semver.org/
[2]: https://conventionalcommits.org
[3]: https://github.com/angular/components/blob/master/CONTRIBUTING.md
[4]: https://github.com/ymind/semantic-gitlog/blob/master/CHANGELOG.md
[5]: https://github.com/ymind/semantic-gitlog/blob/master/config/gitlog/CHANGELOG.tpl.md
[6]: https://github.com/ymind
[7]: https://github.com/skuzzle/semantic-version
[8]: https://github.com/tomasbjerre/git-changelog-lib
[9]: https://opensource.org/licenses/MIT
[10]: http://mustache.github.io/
[11]: https://github.com/ymind/semantic-commit
[12]: https://github.com/ymind/semantic-gitlog
[13]: https://github.com/ymind/maven-semantic-gitlog
[14]: https://github.com/ymind/gradle-semantic-gitlog
