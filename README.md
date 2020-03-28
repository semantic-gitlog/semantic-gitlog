[![Build Status][1010]][1011]
[![GitHub release (latest by date)][1020]][1021]
[![Maven Central][1030]][1031]
[![Semantic Versioning 2.0.0][1040]][1041]
[![Conventional Commits][1050]][1051]
[![GitHub][1060]][1061]

# semantic-gitlog

A simple [Semantic Versioning][1041] management tool based on [Conventional Commits][1051].
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
* [git-changelog-lib][8]

# License

This is open-sourced software licensed under the [MIT license][9].

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

[1010]: https://github.com/semantic-gitlog/semantic-gitlog/workflows/semantic-gitlog/badge.svg?branch=master
[1011]: https://github.com/semantic-gitlog/semantic-gitlog
[1020]: https://img.shields.io/github/v/release/semantic-gitlog/semantic-gitlog
[1021]: https://github.com/semantic-gitlog/semantic-gitlog/releases
[1030]: https://img.shields.io/maven-central/v/team.yi.tools/semantic-gitlog
[1031]: https://search.maven.org/artifact/team.yi.tools/semantic-gitlog
[1040]: https://img.shields.io/badge/Semantic%20Versioning-2.0.0-brightgreen
[1041]: https://semver.org/
[1050]: https://img.shields.io/badge/Conventional%20Commits-1.0.0-yellow.svg
[1051]: https://conventionalcommits.org
[1060]: https://img.shields.io/github/license/semantic-gitlog/semantic-gitlog
[1061]: https://github.com/semantic-gitlog/semantic-gitlog/blob/master/LICENSE
