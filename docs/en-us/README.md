# semantic-gitlog {docsify-ignore-all}

A simple [Semantic Versioning][1] management tool based on [Conventional Commits][2].
It can automatically derive and manage version numbers and generate [angular-style][3] change logs.

The [CHANGELOG.md][4] of this project is automatically generated with this [template][5].

# Features and modules

* Libraries
  - **[semantic-commit][11]**: Lexical Analyzer for `Conventional Commits`
    - Base on `angular-style`
    - Localization support
    - History localization mapping
  - **[semantic-gitlog][12]**: Changelog generator base on `angular-style`
    - Version deriver base on [Semantic Versioning][1]
    - Fully configurable with [Mustache][10]
    - Localization support
    - Multi-template multi-output support
* Plugins
  - [maven-semantic-gitlog][13]
  - [gradle-semantic-gitlog][14]

# Author

[@ymind][6], full stack engineer.

# Related community projects

* [semantic-version][7]
* [git-changelog-lib][8] (Apache-2.0)

# License

This is open-sourced software licensed under the [MIT license][9].

[1]: https://semver.org/
[2]: https://conventionalcommits.org
[3]: https://github.com/angular/components/blob/master/CONTRIBUTING.md
[4]: https://github.com/semantic-gitlog/semantic-gitlog/blob/master/CHANGELOG.md
[5]: https://github.com/semantic-gitlog/semantic-gitlog/blob/master/config/gitlog/CHANGELOG.tpl.md
[6]: https://github.com/ymind
[7]: https://github.com/skuzzle/semantic-version
[8]: https://github.com/tomasbjerre/git-changelog-lib
[9]: https://opensource.org/licenses/MIT
[10]: http://mustache.github.io/
[11]: https://github.com/semantic-gitlog/semantic-commit
[12]: https://github.com/semantic-gitlog/semantic-gitlog
[13]: https://github.com/semantic-gitlog/maven-semantic-gitlog
[14]: https://github.com/semantic-gitlog/gradle-semantic-gitlog
