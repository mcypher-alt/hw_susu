<!DOCTYPE html>
<html <?php language_attributes(); ?>>
<head>
  <meta charset="<?php bloginfo('charset'); ?>">
  <meta name="viewport" content="width=device-width, initial-scale=1.0">
  <link rel="preconnect" href="https://fonts.googleapis.com">
  <link rel="preconnect" href="https://fonts.gstatic.com" crossorigin>
  <?php wp_head(); ?>
</head>
<body <?php body_class('bg-slate-900 text-light'); ?>>
<?php wp_body_open(); ?>

  <header class="sticky-top">
    <nav class="navbar navbar-expand-lg navbar-dark bg-slate-950-80">
      <div class="container">
        <a class="navbar-brand fw-bold text-success" href="<?php echo esc_url(home_url('/')); ?>">HYPER<span class="text-white">LABS</span></a>
        <div class="ms-auto d-flex align-items-center gap-3">
          <?php
          wp_nav_menu([
              'theme_location' => 'primary-menu',
              'container'      => false,
              'menu_class'     => 'navbar-nav flex-row gap-3',
              'fallback_cb'    => false,
              'depth'          => 1
          ]);
          ?>
          <a class="btn btn-outline-success ms-lg-2" href="#contacts">Начать курс</a>
        </div>
      </div>
    </nav>
  </header>