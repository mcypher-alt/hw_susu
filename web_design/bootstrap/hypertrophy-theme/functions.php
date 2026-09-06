<?php
function hypertrophy_setup() {
    add_theme_support('title-tag');
    register_nav_menus([
        'primary-menu' => 'Главное меню'
    ]);
}
add_action('after_setup_theme', 'hypertrophy_setup');

function hypertrophy_scripts() {
    wp_enqueue_style('google-fonts', 'https://fonts.googleapis.com/css2?family=Onest:wght@400;500;600&family=Unbounded:wght@700;900&display=swap', [], null);
    wp_enqueue_style('bootstrap-css', 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/css/bootstrap.min.css', [], '5.3.3');
    wp_enqueue_style('theme-style', get_stylesheet_uri(), ['bootstrap-css'], '1.0');

    wp_enqueue_script('bootstrap-js', 'https://cdn.jsdelivr.net/npm/bootstrap@5.3.3/dist/js/bootstrap.bundle.min.js', [], '5.3.3', true);
    wp_enqueue_script('theme-main-js', get_template_directory_uri() . '/js/main.js', ['bootstrap-js'], '1.0', true);
}
add_action('wp_enqueue_scripts', 'hypertrophy_scripts');