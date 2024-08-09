import logoImg from '@/assets/logo.png';
import Image from 'next/image';
import classes from './main-header.module.css'
import MainHeaderBackground from './main-header-background';
import Link from 'next/link';
import NavLink from './nav-link';

export default function MainHeader() {
  console.log('Execution!!! 경용아!!!');




  return (
    <>
    <MainHeaderBackground />
      <header className={classes.header}>
        <a className={classes.logo} href='/'>
          <Image src={logoImg} alt="A plate with food on it" property=''/>
          NextLevel Food
        </a>

        <nav className={classes.nav}>
          <ul>
            <li>
              <NavLink href="/meals">Browse Meals</NavLink>
            </li>
            <li>
               <NavLink href="/community">Foodies Community</NavLink>
             
            </li>
          </ul>
        </nav>
      </header>
      </>
  );
}