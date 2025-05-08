import React from "react";
import { Outlet } from "react-router-dom";

export default function RootLayout() {
  return (
    <>
      <header>header</header>
      <Outlet></Outlet>
      <footer>footer</footer>
    </>
  );
}