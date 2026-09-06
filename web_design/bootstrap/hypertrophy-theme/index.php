<?php get_header(); ?>

<main>
  <?php if (have_posts()) : while (have_posts()) : the_post(); ?>
    <?php if (get_the_content()) : ?>
      <section class="py-4 bg-slate-950">
        <div class="container text-center">
          <?php the_content(); ?>
        </div>
      </section>
    <?php endif; ?>
  <?php endwhile; endif; ?>

  <section class="hero-section text-center py-5">
    <div class="container py-5">
      <span class="badge bg-success-subtle text-success border border-success mb-3 px-3 py-2">
        Клинически доказанная гипертрофия
      </span>
      <h1 class="display-4 fw-bold mb-4">
        Зачем ждать генетику, если есть <span class="text-success">наука?</span>
      </h1>
      <p class="lead text-secondary mx-auto hero-desc mb-4">
        Инновационный комплекс биохимического доминирования. Пропустите 5 лет застоя в зале за один уверенный сезон.
      </p>
      <button type="button" class="btn btn-success btn-lg px-4 shadow-sm custom-btn" data-bs-toggle="modal" data-bs-target="#dosageModal">
        Рассчитать дозировку
      </button>
    </div>
  </section>

  <section id="stats" class="py-5 bg-black">
    <div class="container">
      <h2 class="text-center fw-bold mb-5">Клинически зафиксированные <span class="text-success">аномалии</span></h2>
      <div class="row text-center g-4">
        <div class="col-md-4">
          <h2 class="display-5 fw-bold text-success">+45%</h2>
          <p class="text-secondary mb-0">К ширине плеч в дверном проеме</p>
        </div>
        <div class="col-md-4">
          <h2 class="display-5 fw-bold text-success">24/7</h2>
          <p class="text-secondary mb-0">Анаболическое окно без закрытия</p>
        </div>
        <div class="col-md-4">
          <h2 class="display-5 fw-bold text-success">0 мин</h2>
          <p class="text-secondary mb-0">Оправданий на тему широкой кости</p>
        </div>
        <div class="col-md-4 mt-md-5">
          <h2 class="display-5 fw-bold text-success">120%</h2>
          <p class="text-secondary mb-0">Тестостерона от уровня серебряной гориллы</p>
        </div>
        <div class="col-md-4 mt-md-5">
          <h2 class="display-5 fw-bold text-success">∞</h2>
          <p class="text-secondary mb-0">Сломанных весов в районной поликлинике</p>
        </div>
        <div class="col-md-4 mt-md-5">
          <h2 class="display-5 fw-bold text-success">3 сек</h2>
          <p class="text-secondary mb-0">Время восстановления после мышечного отказа</p>
        </div>
      </div>
    </div>
  </section>

  <section id="protocols" class="py-5">
    <div class="container">
      <h2 class="text-center fw-bold mb-5">Передовые протоколы</h2>
      <div class="row g-4">
        <div class="col-md-4">
          <div class="card h-100 bg-black border-secondary text-light custom-card">
            <div class="card-body p-4">
              <div class="fs-1 mb-3">⚡</div>
              <h5 class="card-title fw-bold">Мгновенный памп</h5>
              <p class="card-text text-secondary">Кожа натягивается уже при распаковке посылки. Футболки размера M идут на тряпки.</p>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card h-100 bg-black border-secondary text-light custom-card">
            <div class="card-body p-4">
              <div class="fs-1 mb-3">🧪</div>
              <h5 class="card-title fw-bold">Синтез без пауз</h5>
              <p class="card-text text-secondary">Мышцы растут, пока вы листаете мемы. Зал посещать желательно, но чисто из уважения к штанге.</p>
            </div>
          </div>
        </div>
        <div class="col-md-4">
          <div class="card h-100 bg-black border-secondary text-light custom-card">
            <div class="card-body p-4">
              <div class="fs-1 mb-3">🦍</div>
              <h5 class="card-title fw-bold">Челюсть Гигачада</h5>
              <p class="card-text text-secondary">Побочный визуальный эффект: идеальная симметрия лица и способность колоть орехи взглядом.</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

  <section id="reviews" class="py-5 bg-black">
    <div class="container">
      <h2 class="text-center fw-bold mb-5">Подопытные, которыми мы <span class="text-success">гордимся</span></h2>
      <div class="row g-4">
        <div class="col-md-4">
          <div class="card h-100 bg-dark border-secondary text-light custom-card p-0 overflow-hidden">
            <img src="<?php echo get_template_directory_uri(); ?>/images/client1.jpg" class="card-img-top review-cover" alt="Олег">
            <div class="card-body p-4 text-center">
              <h5 class="fw-bold mb-1">Олег «Гипертрофия»</h5>
              <span class="text-success small mb-3 d-block">Стаж курса: 1 сезон</span>
              <p class="text-secondary small mb-0 text-start">«Футболка лопнула прямо в пункте выдачи. Пришлось идти домой топлес.»</p>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card h-100 bg-dark border-secondary text-light custom-card p-0 overflow-hidden">
            <div id="beforeAfterSlider" class="carousel slide" data-bs-ride="carousel">
              <div class="carousel-inner">
                <div class="carousel-item active position-relative">
                  <span class="badge bg-danger position-absolute top-0 start-0 m-3 fs-6 shadow">ДО (гриф 40 кг)</span>
                  <img src="<?php echo get_template_directory_uri(); ?>/images/before.jpg" class="d-block w-100 review-cover" alt="До курса">
                </div>
                <div class="carousel-item position-relative">
                  <span class="badge bg-success position-absolute top-0 start-0 m-3 fs-6 shadow">ПОСЛЕ (машина)</span>
                  <img src="<?php echo get_template_directory_uri(); ?>/images/after.jpg" class="d-block w-100 review-cover" alt="После курса">
                </div>
              </div>
              <button class="carousel-control-prev" type="button" data-bs-target="#beforeAfterSlider" data-bs-slide="prev">
                <span class="carousel-control-prev-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Предыдущий</span>
              </button>
              <button class="carousel-control-next" type="button" data-bs-target="#beforeAfterSlider" data-bs-slide="next">
                <span class="carousel-control-next-icon" aria-hidden="true"></span>
                <span class="visually-hidden">Следующий</span>
              </button>
            </div>

            <div class="card-body p-4 text-center">
              <h5 class="fw-bold mb-1">Илья «Мастодонт»</h5>
              <span class="text-success small mb-3 d-block">Стаж курса: 2 месяца</span>
              <p class="text-secondary small mb-0 text-start">
                «Раньше я был тенью: гриф 40 кг намертво придавливал меня к скамье. Пакеты с кефиром для мамы ощущались как адская фермерская прогулка.
                <br><br>
                После одного протокола я вырвал дверь в парадной. Теперь я несу пакеты, маму и кассиршу на одной руке. Спасибо, наука!»
              </p>
            </div>
          </div>
        </div>

        <div class="col-md-4">
          <div class="card h-100 bg-dark border-secondary text-light custom-card p-0 overflow-hidden">
            <img src="<?php echo get_template_directory_uri(); ?>/images/client3.jpg" class="card-img-top review-cover" alt="Сергей">
            <div class="card-body p-4 text-center">
              <h5 class="fw-bold mb-1">Сергей «Дельта»</h5>
              <span class="text-success small mb-3 d-block">Стаж курса: 4 дня</span>
              <p class="text-secondary small mb-0 text-start">«Мышцы растут быстрее, чем я успеваю покупать новую одежду. Взглядом раскалываю фундук.»</p>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>

  <section id="contacts" class="py-5">
    <div class="container py-5">
      <div class="row justify-content-center">
        <div class="col-md-8 col-lg-6">
          <div class="card bg-black border-secondary text-light custom-card p-4">
            <div class="card-body">
              <h2 class="text-center fw-bold mb-4">Начать <span class="text-success">трансформацию</span></h2>
              <p class="text-center text-secondary mb-4">Оставьте заявку, и наш лаборант свяжется с вами для подбора индивидуального протокола.</p>
              
              <div id="successMessage" class="alert alert-success bg-success-subtle text-success border-success d-none" role="alert">
                <h4 class="alert-heading fw-bold">Заявка принята!</h4>
                <p class="mb-0">Наш специалист уже выехал к вам с партией креатина. Ждите звонка!</p>
              </div>

              <form id="contactForm">
                <div class="mb-3">
                  <label for="userName" class="form-label text-secondary small">Ваше имя (или прозвище в зале)</label>
                  <input type="text" class="form-control bg-dark border-secondary text-light shadow-none" id="userName" placeholder="Например: Артем Трапеция" required>
                </div>
                <div class="mb-4">
                  <label for="userEmail" class="form-label text-secondary small">Электронная почта</label>
                  <input type="email" class="form-control bg-dark border-secondary text-light shadow-none" id="userEmail" placeholder="gigachad@mail.ru" required>
                </div>
                <button type="submit" class="btn btn-success w-100 btn-lg custom-btn">Записаться на курс</button>
              </form>
            </div>
          </div>
        </div>
      </div>
    </div>
  </section>
</main>

<?php get_footer(); ?>