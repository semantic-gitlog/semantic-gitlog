# Changelog

## 0.8.6 (2023-06-02)

### Features

- make tags to be considered configurable ([8b8e791c](https://github.com/semantic-gitlog/semantic-gitlog/commit/8b8e791c0e71cd4c746be95f9d852ffa983ffc88))


### Build System

- **deps**: bump io.freefair.lombok from 5.2.1 to 5.3.3.3 ([2a18d21b](https://github.com/semantic-gitlog/semantic-gitlog/commit/2a18d21b1bfaf65f42378dd0fbb805552c6120f8))
- **deps**: bump junit-jupiter from 5.7.0 to 5.9.3 ([339933c8](https://github.com/semantic-gitlog/semantic-gitlog/commit/339933c811abf92cec320b8383907c6725cae3b8))
- **deps**: bump jackson-databind from 2.11.2 to 2.15.2 ([c8aebc8d](https://github.com/semantic-gitlog/semantic-gitlog/commit/c8aebc8d4159f85dd82c2001d81c73a7dc7548c6))
- **deps**: bump mustache from 0.9.6 to 0.9.10 ([2c4c188b](https://github.com/semantic-gitlog/semantic-gitlog/commit/2c4c188bcaa683a01020b002cc1ad622f66fe4f5))
- **deps**: bump semantic-version from 2.1.0 to 2.1.1 ([42d1cf8e](https://github.com/semantic-gitlog/semantic-gitlog/commit/42d1cf8e71caf11a2f0cd3f35c9f3eb5cf8c11a9))
- **deps**: bump org.eclipse.jgit from 5.9.0.202009080501-r to 5.13.1.202206130422-r ([d39f7643](https://github.com/semantic-gitlog/semantic-gitlog/commit/d39f76436a8ca4a5b3e3fb4133f533e81af0176d))


## 0.7.0 (2020-09-25)

### Bug Fixes

- bump patch when minor or major versions are bumped using `strategy=slow` #8 ([#8](https://github.com/semantic-gitlog/semantic-gitlog/issues/8)) ([950adb3b](https://github.com/semantic-gitlog/semantic-gitlog/commit/950adb3bc6b529d8ebc2a4de788cd5107b490255))


### Features

- support custom release sections order ([0cfe8cb8](https://github.com/semantic-gitlog/semantic-gitlog/commit/0cfe8cb85dd59cd31b274b6a1b61063e944526d8))


### Code Refactoring

- code optimization ([77b1b873](https://github.com/semantic-gitlog/semantic-gitlog/commit/77b1b8731cd481ec525d8c2611bd4e1f353d612b))
- rename VersionStrategies to VersionStrategy ([85bfaee8](https://github.com/semantic-gitlog/semantic-gitlog/commit/85bfaee8efcc5e9f322b544e9fe9a04624a0a086))


### Build System

- **deps**: bump io.freefair.lombok from 5.1.0 to 5.2.1 ([38ddbb09](https://github.com/semantic-gitlog/semantic-gitlog/commit/38ddbb099fe3d8c17566982ecc732149d1a16c14))
- **deps**: bump team.yi.semantic-gitlog from 0.5.3 to 0.5.13 ([840860f8](https://github.com/semantic-gitlog/semantic-gitlog/commit/840860f8295b84002c14ecf21b0606105579eef9))
- **deps**: bump org.eclipse.jgit from 5.8.1.202007141445-r to 5.9.0.202009080501-r ([662f44dd](https://github.com/semantic-gitlog/semantic-gitlog/commit/662f44dd48cc330194290e3b75a3de0d3a3e9d81))
- **deps**: bump jackson-databind from 2.11.1 to 2.11.2 ([997903f6](https://github.com/semantic-gitlog/semantic-gitlog/commit/997903f62147926a3a547b7218b3d0b0347937e2))
- **deps**: bump junit-jupiter from 5.6.2 to 5.7.0 ([b72fa18e](https://github.com/semantic-gitlog/semantic-gitlog/commit/b72fa18e64c6c92f193f5659dc0b27ddc130136b))
- **deps**: bump checkstyle from 8.35 to 8.36.1 ([9a6f837a](https://github.com/semantic-gitlog/semantic-gitlog/commit/9a6f837aa5c2d1b27e8bee94c3352510df1b208b))
- **deps**: bump pmd from 6.26.0 to 6.27.0 ([08b4eb79](https://github.com/semantic-gitlog/semantic-gitlog/commit/08b4eb79b59be9fe483f067c6dd1eecace8cbc33))
- **deps**: bump semantic-commit from 0.5.7 to 0.5.13 ([f12a7c5a](https://github.com/semantic-gitlog/semantic-gitlog/commit/f12a7c5ad0035dfcf1eaba66478056162b5a725e))
- **gradle**: bumped gradle version from 6.5.1 to 6.6.1 ([10b5f902](https://github.com/semantic-gitlog/semantic-gitlog/commit/10b5f902120055280c4eb3e88b3611c6a06ad7ce))


## 0.6.1 (2020-08-30)

### Features

- **config**: implement option `strategy` ([0184d6a8](https://github.com/semantic-gitlog/semantic-gitlog/commit/0184d6a859bcea9765e6637f3bc40538a2966320))


### Build System

- **deps**: bump team.yi.semantic-gitlog from 0.5.3 to 0.5.12 ([6563d996](https://github.com/semantic-gitlog/semantic-gitlog/commit/6563d996ae1829abadd9624675b991143cbd1a12))


## 0.5.11 (2020-08-01)

### Code Refactoring

- **render**: display scope.name when scope.displayName is empty ([675a3d65](https://github.com/semantic-gitlog/semantic-gitlog/commit/675a3d653240b81e4d1b39c67b4b1253891fa094))


### Styles

- **editorconfig**: update .editorconfig, disable .mustache file indent ([50357d7c](https://github.com/semantic-gitlog/semantic-gitlog/commit/50357d7c34e03d693f944c5b9cc28134ca8c4420))


### Documentation

- **docs**: update docs ([d09195a5](https://github.com/semantic-gitlog/semantic-gitlog/commit/d09195a5609c6aad2d8be0a7e622ad3a4e019ba0))


### Build System

- **deps**: bump io.freefair.lombok from 4.1.6 to 5.1.0 ([324e42a4](https://github.com/semantic-gitlog/semantic-gitlog/commit/324e42a460959baae89b7d7b15351634106d4105))
- **deps**: bump team.yi.semantic-gitlog from 0.4.1.1 to 0.5.3 ([e4fbe583](https://github.com/semantic-gitlog/semantic-gitlog/commit/e4fbe583dadf3dfeb12c8ad841317eb19fea23c4))
- **deps**: bump junit-jupiter from 5.6.1 to 5.6.2 ([2870fd12](https://github.com/semantic-gitlog/semantic-gitlog/commit/2870fd122b746f64aa056787fb3028d2956faa66))
- **deps**: bump jackson-databind from 2.10.3 to 2.11.1 ([314057de](https://github.com/semantic-gitlog/semantic-gitlog/commit/314057de80aa0f3518ea985a90fdaf3652c9f5b4))
- **deps**: bump org.eclipse.jgit ([1612aed1](https://github.com/semantic-gitlog/semantic-gitlog/commit/1612aed155980d5c67bae81bf1bf4df17b94c120))
- **deps**: bump checkstyle from 8.30 to 8.35 ([89b87d8e](https://github.com/semantic-gitlog/semantic-gitlog/commit/89b87d8eaa6cb4ddeae11cf173f13c09a440ecae))
- **deps**: bump pmd from 6.22.0 to 6.26.0 ([885915b2](https://github.com/semantic-gitlog/semantic-gitlog/commit/885915b25aee1c9477bb6e4f07aee7ebea1005ea))
- **deps**: bump semantic-commit from 0.5.0 to 0.5.7 ([38091280](https://github.com/semantic-gitlog/semantic-gitlog/commit/3809128097e76348b2493ba05c7f09e416a8167d))
- **deps**: bump gradle-wrapper from 6.3 to 6.5.1 ([1dc621fa](https://github.com/semantic-gitlog/semantic-gitlog/commit/1dc621faacee2c807b0ddb3d1e06b0fec98dc167))


### Continuous Integration

- **github**: add github workflows ([0d6c866b](https://github.com/semantic-gitlog/semantic-gitlog/commit/0d6c866b7c4649a7b85ec53039b462f2a1be4807))


## 0.5.0 (2020-03-28)

### Features

- **service**: implement primary features and challenges ([d955c57f](https://github.com/semantic-gitlog/semantic-gitlog/commit/d955c57f7df0284649cfe00c4a7aea6ce0d8a17f))

