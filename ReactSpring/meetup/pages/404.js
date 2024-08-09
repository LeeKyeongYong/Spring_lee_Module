// pages/404.js
import Link from 'next/link';
import styles from './404.module.css'; // CSS 모듈 import

export default function Custom404() {
  return (
    <main className={styles.container}>
      <h1 className={styles.title}>404 - Page Not Found</h1>
      <p className={styles.message}>Sorry, the page you are looking for does not exist.</p>
      <Link href="/">Go back to the homepage</Link>
    </main>
  );
}
