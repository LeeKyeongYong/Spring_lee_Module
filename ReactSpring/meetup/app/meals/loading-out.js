// src/util/loading.js
import classes from './loading.module.css'; // CSS 파일 경로 확인

export default function Loading() {
    return <p className={classes.loading}>Loading...</p>;
}
