import type { Config } from "tailwindcss";

export default {
    darkMode: ["class"],
    content: [
    "./src/pages/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/components/**/*.{js,ts,jsx,tsx,mdx}",
    "./src/app/**/*.{js,ts,jsx,tsx,mdx}",
  ],
  theme: {
	  extend: {
		  colors: {
			  background: "hsl(var(--background))",
			  foreground: "hsl(var(--foreground))",
			  card: {
				  DEFAULT: "hsl(var(--card))",
				  foreground: "hsl(var(--card-foreground))",
			  },
			  popover: {
				  DEFAULT: "hsl(var(--popover))",
				  foreground: "hsl(var(--popover-foreground))",
			  },
			  primary: {
				  DEFAULT: "hsl(var(--primary))",
				  foreground: "hsl(var(--primary-foreground))",
			  },
			  secondary: {
				  DEFAULT: "hsl(var(--secondary))",
				  foreground: "hsl(var(--secondary-foreground))",
			  },
			  muted: {
				  DEFAULT: "hsl(var(--muted))",
				  foreground: "hsl(var(--muted-foreground))",
			  },
			  accent: {
				  DEFAULT: "hsl(var(--accent))",
				  foreground: "hsl(var(--accent-foreground))",
			  },
			  destructive: {
				  DEFAULT: "hsl(var(--destructive))",
				  foreground: "hsl(var(--destructive-foreground))",
			  },
			  border: "hsl(var(--border))",
			  input: "hsl(var(--input))",
			  ring: "hsl(var(--ring))",
			  chart: {
				  "1": "hsl(var(--chart-1))",
				  "2": "hsl(var(--chart-2))",
				  "3": "hsl(var(--chart-3))",
				  "4": "hsl(var(--chart-4))",
				  "5": "hsl(var(--chart-5))",
			  },
		  },
		  borderRadius: {
			  lg: "var(--radius)",
			  md: "calc(var(--radius) - 2px)",
			  sm: "calc(var(--radius) - 4px)",
		  },
	  },
  },
	plugins: [
		require("tailwindcss-animate"),
		function ({ addVariant }: { addVariant: any }) {
			const groupVariantPrefixes = ["p1", "p2", "p3"];

			function escapeClassName(className: string) {
				return className.replace(/[\[\]\(\)\+\*\#\/\%]/g, "\\$&");
			}
			// group-on 변형 추가 함수
			const addGroupOnVariant = (name = "") => {
				const selector = name ? `group\\/${name}` : "group";
				const variantName = name ? `group-on/${name}` : "group-on";
				addVariant(
					variantName,
					({ modifySelectors }: { modifySelectors: any }) => {
						modifySelectors(({ className }: { className: any }) => {
							return `.${selector}.on .${escapeClassName(
								variantName
							)}\\:${escapeClassName(className)}`;
						});
					}
				);
			};
			// peer-on 변형 추가 함수
			const addPeerOnVariant = (name = "") => {
				const selector = name ? `peer\\/${name}` : "peer";
				const variantName = name ? `peer-on/${name}` : "peer-on";
				addVariant(
					variantName,
					({ modifySelectors }: { modifySelectors: any }) => {
						modifySelectors(({ className }: { className: any }) => {
							return `.${selector}.on ~ .${escapeClassName(
								variantName
							)}\\:${escapeClassName(className)}`;
						});
					}
				);
			};
			// 기본 group-on, peer-on 변형 추가
			addGroupOnVariant();
			addPeerOnVariant();
			// 특정 그룹과 피어에 대한 변형 추가
			groupVariantPrefixes.forEach((name) => {
				addGroupOnVariant(name);
				addPeerOnVariant(name);
			});
			// on 클래스에 대한 변형 추가
			addVariant("on", ({ modifySelectors }: { modifySelectors: any }) => {
				modifySelectors(({ className }: { className: any }) => {
					return `.on.on\\:${escapeClassName(className)}`;
				});
			});
		},
	],
} satisfies Config;
