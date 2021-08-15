'use strict';

const gulp = require('gulp');
const sass = require('gulp-sass');
const imagemin = require('gulp-imagemin');
const cleanCSS = require('gulp-clean-css');
const autoprefixer = require('gulp-autoprefixer');
const rename = require("gulp-rename");
// these two are for js boundle
const concat = require('gulp-concat');
const minify = require('gulp-minify');
// RUN CMD TO GENERATE FONT ICON
const exec = require('child_process').exec;
// these two are for gulp dest more
var multiDest = require('gulp-multi-dest');
var destOptions = {
  mode: parseInt('0755')
};
// -------------------------------------------------------------
// START MANAGE CSS - FROM TO
// -------------------------------------------------------------
const css_files = ['./src/main/resources/static/assets/scss/admin/layout/hyper-admin-layout-style.scss' , './src/main/resources/static/assets/scss/admin/blog/hyper-blog-style.scss'];
const css_multi_dest = ['./src/main/resources/static/assets/css'];
// -----------------------------
gulp.task('sass', function () {
  return gulp.src(css_files)
    .pipe(sass())
    .pipe(autoprefixer({
      cascade: false
    }))
    .pipe(multiDest(css_multi_dest, destOptions))
  // .pipe(gulp.dest('./assets/css'))
});
gulp.task('sass-min', function () {
  return gulp.src(css_files)
    .pipe(sass())
    .pipe(autoprefixer({
      cascade: false
    }))
    .pipe(cleanCSS({ compatibility: 'ie8' }))
    .pipe(rename({
      suffix: '.min'
    }))
    .pipe(multiDest(css_multi_dest, destOptions))
  // .pipe(gulp.dest('./assets/css'))
});
// RUN CMD TO GENERATE FONT ICON
gulp.task('cmd-gulp-font-icon', function (cb) {
  exec('icon-font-generator ./src/main/resources/static/assets/images/svg-icon/*.svg -o ./src/main/resources/static/assets/fonts/font-icon -n fonticon', function (err, stdout, stderr) {
    console.log(stdout);
    console.log(stderr);
    cb(err);
  });
});
gulp.task('watch', function () {
  gulp.watch('./src/main/resources/static/assets/scss/**/*.scss', gulp.series(['sass', 'sass-min']));
  gulp.watch('./src/main/resources/static/assets/images/svg-icon', gulp.series(['cmd-gulp-font-icon']));
});
// -------------------------------------------------------------
// GULP IMAGE MIN
// -------------------------------------------------------------
gulp.task('svg', async function () {
  gulp.src('./src/main/resources/static/assets/images/svg-icon/*.svg')
    .pipe(imagemin([
      imagemin.svgo({
        plugins: [
          { removeViewBox: false },
          { cleanupIDs: false }]
      })
    ]))
    .pipe(gulp.dest('./src/main/resources/static/assets/gulp-image/svg-icon'))
});

gulp.task('image', async function () {
  gulp.src('./src/main/resources/static/assets/images/*.{jpg,jpeg,png}')
    .pipe(imagemin())
    .pipe(gulp.dest('./src/main/resources/static/assets/gulp-image'))
});
gulp.task('imagemin', gulp.series(['svg', 'image']));
// -------------------------------------------------------------
// GULP CONCAT JS
// -------------------------------------------------------------
const js_concat_path = {
  input_file: [
    {
      'filename': 'admin-layout',
      'path': ['./src/main/resources/static/assets/js/swiper-bundle.min.js']
    }
  ],
  dest: './src/main/resources/static/assets/bundle-js'
}
gulp.task('js-concat-0', function () {
  return gulp.src(js_concat_path.input_file[0].path)
    .pipe(concat('hyper-' + js_concat_path.input_file[0].filename + '-functions.js'))
    .pipe(gulp.dest(js_concat_path.dest))
});
gulp.task('js-minify-theme-all', function () {
  return gulp.src(js_concat_path.dest + '/*.js')
    .pipe(minify())
    .pipe(gulp.dest(js_concat_path.dest + '/min'))
});
gulp.task('js-concat-full', gulp.series(['js-concat-0', 'js-minify-theme-all']));

// -------------------------------------------------------------
// GULP CONCAT CSS
// -------------------------------------------------------------
const css_concat_path = {
  input_file: [
    {
      'filename': 'admin-layout',
      'path': ['./src/main/resources/static/assets/css/style.css']
    }
  ],
  dest: './src/main/resources/static/assets/bundle-css'
}
gulp.task('css-concat-0', function () {
  return gulp.src(css_concat_path.input_file[0].path)
    .pipe(concatCss('hyper-' + css_concat_path.input_file[0].filename + '-style.css'))
    .pipe(gulp.dest(css_concat_path.dest));
});
gulp.task('minify-theme-all', function () {
  return gulp.src('./src/main/resources/static/assets/bundle-css/*.css')
    .pipe(cleanCSS({ compatibility: 'ie8' }))
    .pipe(rename({
      suffix: '.min'
    }))
    .pipe(gulp.dest('./src/main/resources/static/assets/bundle-css/min'))
});
gulp.task('css-concat-full', gulp.series(['css-concat-0', 'minify-theme-all']));