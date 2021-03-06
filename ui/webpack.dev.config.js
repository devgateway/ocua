var webpack = require('webpack');
var path = require('path');
module.exports = {
  entry: [
    'webpack-dev-server/client?http://localhost:3000',
    'webpack/hot/only-dev-server',
    './index.jsx'
  ],
  output: {
    path: path.join(__dirname, 'public/ui'),
    publicPath: "http://localhost:3000/",
    filename: "index.js"
  },
  module: {
    loaders: [
      {
        test: /\.(jsx|es6)$/,
        loaders: [
          'react-hot',
          'babel-loader?babelrc=true,cacheDirectory'
        ],
        exclude: /node_modules/
      },
      { test: /\.json$/, loader: 'json' },
      { test: /\.css$/, exclude: /\.useable\.css$/, loader: "style!css" },
      { test: /\.less$/, loader: "style!css!less" }
    ]
  },
  resolve: {
    extensions: ['', '.js', '.es6', '.jsx']
  },
  devtool: 'source-map',
  plugins: [
    new webpack.DefinePlugin({
      "process.env": {
        NODE_ENV: JSON.stringify("development")
      }
    }),
    new webpack.DllReferencePlugin({
      context: __dirname,
      name: 'lib',
      manifest: require('./dll/manifest.json')
    }),
    new webpack.HotModuleReplacementPlugin(),
    new webpack.ProvidePlugin({
      fetch: 'imports?this=>global!exports?global.fetch!whatwg-fetch',
      React: "react",
    })
  ],
  eslint:{
    configFile: "./.eslintrc"
  }
};
